package org.example.other.network.niochat;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WebchatTest {

    @Test
    void testSelector() throws IOException, InterruptedException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        new Thread(() -> {
            while (true) {
                try {
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        System.out.println(key);
                        if (key.isAcceptable()) register(selector, serverSocket);
                        if (key.isReadable()) echo(buffer, key);
                        iterator.remove();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(1000);

        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5454));

        ByteBuffer clientBuffer = ByteBuffer.allocate(32);

        for (int i =0;i<1;i++){
            String s = "Message #"+i;

            clientBuffer.put(s.getBytes());
            clientBuffer.flip();
            client.write(clientBuffer);
            clientBuffer.clear();

            int read = client.read(clientBuffer);
            System.out.println("Response: "+new String(clientBuffer.array(), 0,read ));
            clientBuffer.clear();
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
         client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("Registered client "+client.getRemoteAddress());
    }

    private static void echo(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        int read = client.read(buffer);
        String data = new String(buffer.array());
        System.out.println("Data read on server: "+data);
        if(read == -1 || new String(buffer.array()).trim().equalsIgnoreCase("stop")){
            client.close();
            System.out.println("Closed connection with "+client);
        } else{
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }
    }

}