package org.example.other.network.niochat;

import org.example.other.network.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NioServerTest {

    @Test
    void testServer() throws IOException, InterruptedException {
        NioServer nioServer = new NioServer();
        nioServer.init("localhost", 5454);

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Map<String, Object> data = Map.of("type", "init", "username", "grocer");
        buffer.put(JsonParser.toJson(data).getBytes());
        buffer.flip();
        socketChannel.write(buffer);

        buffer.clear();
        int read = socketChannel.read(buffer);
        System.out.println(new String(buffer.array(),0,read));

        Thread.sleep(1000);

        SocketChannel socketChannel2 = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        ByteBuffer buffer2 = ByteBuffer.allocate(512);
        Map<String, Object> data2 = Map.of("type", "init", "username", "freedeeml");
        buffer2.put(JsonParser.toJson(data2).getBytes());
        buffer2.flip();
        socketChannel2.write(buffer2);

        buffer2.clear();
        int read2 = socketChannel2.read(buffer2);
        System.out.println(new String(buffer2.array(),0,read2));

        Map<String, Object> messageMap = Map.of("type", "message", "target", "grocer", "message", "you are lox");
    }

}