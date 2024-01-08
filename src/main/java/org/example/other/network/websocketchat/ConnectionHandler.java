package org.example.other.network.websocketchat;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.other.network.JsonParser;

import java.net.Socket;
import java.util.Map;

@Log4j2
class ConnectionHandler {
    ChatSocket socket;
    WebSocketChat chat;

    public ConnectionHandler(Socket socket, WebSocketChat chat) {
        this.socket = new ChatSocket(socket);
        this.chat = chat;
        log.info("SERVER: New connection detected " + socket + ". Validating...");
        new Thread(this::validateClient).start();
    }

    @SneakyThrows
    private void validateClient() {
        log.info("SERVER: Waiting for init message");
        String message = socket.receiveMessage();
        Map<String, Object> map = JsonParser.parse(message);
        log.info("SERVER: Received message: " + message);

        if ("init".equals(map.get("type"))){
            String username = (String) map.get("origin");
            if(chat.users.containsKey(username)){
                log.info("SERVER: User already exists");
                socket.sendMessage(JsonParser.toJson(Map.of("type", "error", "payload", "User already exists")));
                socket.close();
            } else {
                log.info("SERVER: User " + username + " validated");
                chat.users.put(username, new ChatUser(username, socket, chat));
                socket.sendMessage(JsonParser.toJson(Map.of("type", "ack", "payload", "User validated")));
            }
        } else{
            log.info("SERVER: Invalid message");
            socket.sendMessage(JsonParser.toJson(Map.of("type", "error", "payload", "Invalid message")));
            socket.close();
        }

    }

}
