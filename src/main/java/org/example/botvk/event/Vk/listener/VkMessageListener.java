package org.example.botvk.event.Vk.listener;

import lombok.RequiredArgsConstructor;
import org.example.botvk.event.Vk.dto.VkMessageEvent;
import org.example.botvk.service.impl.BotVkServiceImpl;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VkMessageListener {

    private final BotVkServiceImpl botVkService;

    @Async("asyncExecutor")
    @EventListener
    public void onVkMessageEvent(VkMessageEvent event) {
        String response = String.format("Вы написали: %s", event.getText());
        botVkService.sendMessage(event.getUserId(), response);
    }
}
