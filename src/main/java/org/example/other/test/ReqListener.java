package org.example.other.test;

import lombok.RequiredArgsConstructor;
import org.example.other.events.base.EventHandler;
import org.example.other.events.base.EventListener;
import org.example.other.network.JsonParser;

import java.util.Map;

@RequiredArgsConstructor
public class ReqListener implements EventListener {

    final MessageProducer responseProducer;

    @EventHandler
    public void onMessage(MessageEvent event){

        if(!event.topic.equals("request")) return;
        Map<String, Object> data = JsonParser.parse(event.message);

        int bound = Integer.parseInt((String) data.get("number"));
        int result = Computator.doComputation(bound);

        String responseData = JsonParser.toJson(Map.of("random", result));
        responseProducer.send(event.key, responseData);
    }

}
