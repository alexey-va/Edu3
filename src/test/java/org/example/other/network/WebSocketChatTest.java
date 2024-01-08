package org.example.other.network;

import lombok.extern.log4j.Log4j2;
import org.example.other.network.websocketchat.ChatClient;
import org.example.other.network.websocketchat.WebSocketChat;
import org.junit.jupiter.api.Test;

@Log4j2
class WebSocketChatTest {


    @Test
    void testInit() throws InterruptedException {
        WebSocketChat chat = new WebSocketChat(6868);
        chat.init();

        ChatClient chatClient = new ChatClient("Grocer", "localhost", 6868);
        chatClient.initConnection();

        Thread.sleep(1);

        ChatClient chatClient2 = new ChatClient("Bobby", "localhost", 6868);
        chatClient2.initConnection();


        Thread.sleep(100);
        System.out.println();
        log.info("Sending messages\n");

        chatClient.sendMessage("Hello from Grocer to Bobby", "Bobby");
        chatClient2.sendMessage("Hello from Bobby to Grocer", "Grocer");
    }

}