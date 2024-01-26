package org.example.other.test;

import org.example.other.events.base.EventHandler;
import org.example.other.events.base.EventListener;
import org.example.other.network.JsonParser;

import java.util.Map;
import java.util.UUID;

public class RespListener implements EventListener {

    @EventHandler
    public void onMessage(MessageEvent event){
        if(!event.topic.equals("response")) return;
        Map<String, Object> data = JsonParser.parse(event.message);
        UUID uuid = UUID.fromString(event.key);
        int result = Integer.parseInt((String) data.get("random"));

        RequestManager.processResponse(uuid, result);
    }

}
