package org.example.other.network.websocketchat;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.other.events.base.EventManager;
import org.example.other.network.JsonParser;

import java.util.Map;

@Log4j2
public class ChatUser {
    final String name;
    final ChatSocket socket;
    WebSocketChat chat;

    public ChatUser(String name, ChatSocket socket, WebSocketChat chat) {
        this.name = name;
        this.socket = socket;
        this.chat=chat;
        new Thread(this::receiveMessage).start();
    }

    public void sendMessage(String origin, String payload) {
        try {
            Map<String, Object> map = Map.of("type", "message", "payload", payload, "origin", origin, "target", name);
            socket.sendMessage(JsonParser.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    public void receiveMessage() {
        String message = socket.receiveMessage();
        Map<String, Object> map = JsonParser.parse(message);
        ChatMessageEvent event = ChatMessageEvent.builder()
                .chatUser(this)
                .origin(map.get("origin").toString())
                .payload(map.get("payload").toString())
                .target(map.get("target").toString())
                .build();
        EventManager.getInstance().run(event);
    }


    public void disconnect() {
        this.socket.close();
    }
}
