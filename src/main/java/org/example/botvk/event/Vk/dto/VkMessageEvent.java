package org.example.botvk.event.Vk.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VkMessageEvent extends ApplicationEvent {
    private int userId;
    private String text;

    @Builder
    public VkMessageEvent(Object source, int userId, String text) {
        super(source);
        this.userId = userId;
        this.text = text;
    }
}
