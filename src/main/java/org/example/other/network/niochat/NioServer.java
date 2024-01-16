package org.example.other.network.niochat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.other.network.JsonParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer extends Webchat {
    Selector selector;
    ServerSocketChannel serverSocket;
    Thread serverThread;
    ConcurrentHashMap<String, WebchatClient> clients = new ConcurrentHashMap<>();
    ConnectionHandler connectionHandler;
    MessageHandler messageHandler;


    @Override
    public void init(String address, int port) {
        connectionHandler = new ConnectionHandler(this);
        messageHandler = new MessageHandler(this);

        try {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(address, port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            serverThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        selector.select();
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = keys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isAcceptable()) connectionHandler.handleConnection(serverSocket, selector);
                            if (key.isReadable()) messageHandler.handleMessage(key);
                            iterator.remove();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "webchat-thread");
            serverThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(WebchatClient from, WebchatClient to, String message) {
        Instant instant = Instant.now();
        Map<String, Object> data = Map.of("type", "message", "message", message, "origin", from.username, "created_at", instant.toString());
        String payload = JsonParser.toJson(data);

        ByteBuffer buffer = ByteBuffer.wrap(payload.getBytes());
        try {
            to.socketChannel.write(buffer);
            ChatMessage chatMessage = new ChatMessage(message, from.username, instant, to.username);
            from.receivedMessages.add(chatMessage);
            to.sentMessages.add(chatMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String from, String to, String message) {
        WebchatClient c1 = clients.get(from);
        WebchatClient c2 = clients.get(to);
        sendMessage(c1, c2, message);
    }

    @Override
    public void stop() {

    }
}

@RequiredArgsConstructor
class ConnectionHandler {
    private final NioServer server;
    private final ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

    public void handleConnection(ServerSocketChannel serverSocketChannel, Selector selector) {
        service.submit(() -> checkConnection(serverSocketChannel, selector));
    }

    private void checkConnection(ServerSocketChannel serverSocketChannel, Selector selector) {
        try {
            SocketChannel channel = serverSocketChannel.accept();
            if (channel == null) return;
            //System.out.println("Trying to establish connection with " + channel.getRemoteAddress());

            ByteBuffer buffer = ByteBuffer.allocate(512);
            int read = channel.read(buffer);
            String data = new String(buffer.array(), 0, read);
            Map<String, Object> json = JsonParser.parse(data);

            String username = (String) json.get("username");
            String type = (String) json.get("type");

            if (type == null || username == null || !type.equals("init")) {
                declineConnection(channel, DeclineReason.INCORRECT_PROTOCOL);
            } else if (server.clients.containsKey(username)) {
                declineConnection(channel, DeclineReason.USERNAME_TAKEN);
            } else {

                acceptConnection(channel, selector, username);

                server.clients.put(username, WebchatClient.builder()
                        .username(username)
                        .socketChannel(channel)
                        .build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void declineConnection(SocketChannel socketChannel, DeclineReason declineReason) {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Map<String, Object> response = Map.of("result", "error", "reason", declineReason.message);
        System.out.println("Declining connection! " + JsonParser.toJson(response));
        buffer.put(JsonParser.toJson(response).getBytes());
        buffer.flip();
        try {
            socketChannel.write(buffer);
            socketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptConnection(SocketChannel socketChannel, Selector selector, String username) {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Map<String, Object> response = Map.of("result", "success");
        //System.out.println("Accepting connection! "+JsonParser.toJson(response));
        buffer.put(JsonParser.toJson(response).getBytes());
        buffer.flip();
        try {
            socketChannel.write(buffer);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ, username);
        } catch (IOException e) {
            System.out.println("Rip bozo!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    enum DeclineReason {
        INCORRECT_PROTOCOL("Incorrect protocol"), USERNAME_TAKEN("Username already taken");

        final String message;

        DeclineReason(String message) {
            this.message = message;
        }
    }

}

class MessageHandler {
    NioServer server;
    ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

    MessageHandler(NioServer nioServer) {
        this.server = nioServer;
    }

    public void handleMessage(SelectionKey selectionKey) {
        service.submit(() -> process(selectionKey));
    }

    private void process(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            String username = (String) selectionKey.attachment();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read;
            StringBuilder stringBuilder = new StringBuilder();

            while ((read = socketChannel.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer.array(), 0, read));
            }

            Map<String, Object> data = JsonParser.parse(stringBuilder.toString());

            String type = (String) data.get("type");
            if (type == null) sendErrorMessage(socketChannel, ErrorReason.NO_TYPE_SPECIFIED);
            else if (type.equalsIgnoreCase("disconnect")) disconnect(username);
            else if (type.equalsIgnoreCase("message")) {
                String target = (String) data.get("target");
                if (target == null) {
                    sendErrorMessage(socketChannel, ErrorReason.NO_TARGET_USER_SPECIFIED);
                    return;
                }
                if (!server.clients.containsKey(target)) {
                    sendErrorMessage(socketChannel, ErrorReason.NO_SUCH_USER_ONLINE);
                    return;
                }

                String message = (String) data.get("message");
                if (message == null) {
                    sendErrorMessage(socketChannel, ErrorReason.NO_MESSAGE_SPECIFIED);
                    return;
                }
                server.sendMessage(username, target, message);
            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendErrorMessage(SocketChannel socketChannel, ErrorReason reason) {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Map<String, Object> response = Map.of("result", "error", "reason", reason.toString());

        buffer.put(JsonParser.toJson(response).getBytes());
        buffer.flip();
        try {
            socketChannel.write(buffer);
            socketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void disconnect(String username) {
        WebchatClient client = server.clients.get(username);
        if (client == null) return;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            Map<String, Object> response = Map.of("result", "success");
            buffer.put(JsonParser.toJson(response).getBytes());
            buffer.flip();

            client.socketChannel.write(buffer);
            client.socketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.clients.remove(username);
    }

    enum ErrorReason {
        NO_TYPE_SPECIFIED, NO_SUCH_USER_ONLINE, NO_MESSAGE_SPECIFIED, NO_TARGET_USER_SPECIFIED
    }
}

@Builder
class WebchatClient {
    String username;
    SocketChannel socketChannel;
    @Builder.Default
    List<ChatMessage> receivedMessages = new ArrayList<>();
    @Builder.Default
    List<ChatMessage> sentMessages = new ArrayList<>();
}

@AllArgsConstructor
class ChatMessage {
    String payload;
    String sender;
    Instant receivedAt;
    String receiver;
}
