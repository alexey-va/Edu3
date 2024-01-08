package org.example.other.network.websocketchat;

import lombok.extern.log4j.Log4j2;
import org.example.other.network.JsonParser;

import java.net.Socket;
import java.util.Map;

@Log4j2
public class ChatClient {

    Thread thread;
    ChatSocket socket;
    String name;
    boolean init = false;

    public ChatClient(String name, String host, int port) {
        this.name = name;
        try {
            socket = new ChatSocket(new Socket(host, port));
            thread = new Thread(this::receiveMessages);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveMessages() {
        while (true) {
            try {
                String message = socket.receiveMessage();
                log.info(name+": Received message: " + message);
                if(message.contains("ack")){
                    init = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            Map<String, Object> map = Map.of("type", "message", "payload", message, "origin", name);
            socket.sendMessage(JsonParser.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, String target){
        try {
            Map<String, Object> map = Map.of("type", "message", "payload", message, "origin", name, "target", target);
            socket.sendMessage(JsonParser.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initConnection(){
        if(init)return;
        try {
            Map<String, Object> map = Map.of("type", "init", "origin", name);
            //log.info(name+": Sending init message: " + JsonParser.toJson(map));
            socket.sendMessage(JsonParser.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
