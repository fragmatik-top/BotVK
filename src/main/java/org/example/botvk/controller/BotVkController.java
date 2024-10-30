package org.example.botvk.controller;

import com.json.botVk.VkRequest;
import lombok.RequiredArgsConstructor;
import org.example.botvk.event.Vk.publisher.VkEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotVkController {
    private final VkEventPublisher vkEventPublisher;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handlerEvent(@RequestBody VkRequest vkRequest) {
        if ("confirmation".equals(vkRequest.getType())) {
            return ResponseEntity.ok("3c6d9d27");
        }
        vkEventPublisher.publishEvent(vkRequest);
        return ResponseEntity.ok("ok");
    }
}
