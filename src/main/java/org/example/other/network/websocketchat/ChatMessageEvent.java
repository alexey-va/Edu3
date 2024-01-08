package org.example.other.network.websocketchat;

import lombok.Builder;
import lombok.Data;
import org.example.other.events.base.Event;

@Data
@Builder
public class ChatMessageEvent extends Event {

    ChatUser chatUser;
    String payload;
    String origin;
    String target;


}
