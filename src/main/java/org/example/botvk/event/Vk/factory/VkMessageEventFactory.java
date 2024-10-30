package org.example.botvk.event.Vk.factory;

import com.json.botVk.VkRequest;
import org.example.botvk.event.Vk.dto.VkMessageEvent;
import org.springframework.stereotype.Component;

@Component
public class VkMessageEventFactory implements VkEventFactory<VkMessageEvent> {
    @Override
    public boolean supports(String type) {
        return type.equals("message_new");
    }

    @Override
    public VkMessageEvent createEvent(VkRequest request) {
        return VkMessageEvent.builder()
                .source(this)
                .userId(request.getObject().getMessage().getFromId())
                .text(request.getObject().getMessage().getText())
                .build();
    }
}
