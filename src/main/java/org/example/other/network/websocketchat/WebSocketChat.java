package org.example.other.network.websocketchat;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.other.events.base.EventHandler;
import org.example.other.events.base.EventListener;
import org.example.other.events.base.EventManager;

import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Log4j2
public class WebSocketChat implements EventListener {

    final int port;
    ServerSocket socket;
    ConcurrentHashMap<String, ChatUser> users = new ConcurrentHashMap<>();

    @SneakyThrows
    public void init() {
        socket = new ServerSocket(port);
        EventManager.getInstance().registerListener(this);
        new Thread(this::acceptConnections).start();
    }

    private void acceptConnections() {
        log.info("SERVER: Server started on port " + port);
        try {
            while (true) {
                new ConnectionHandler(socket.accept(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMessageReceived(ChatMessageEvent event) {
        if (event.getTarget() == null) log.info("SERVER: " + event.getOrigin() + ": " + event.getPayload());
        else users.get(event.getTarget()).sendMessage(event.getOrigin(), event.getPayload());
    }


}
