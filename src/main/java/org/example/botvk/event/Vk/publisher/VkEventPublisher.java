package org.example.botvk.event.Vk.publisher;

import com.json.botVk.VkRequest;
import lombok.RequiredArgsConstructor;
import org.example.botvk.event.Vk.factory.VkEventFactory;
import org.example.botvk.exception.DataValidationException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VkEventPublisher {
    private final List<VkEventFactory<?>> eventFactories;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    public void publishEvent(VkRequest request) {
        eventFactories.stream()
                .filter(factory -> factory.supports(request.getType()))
                .findFirst()
                .ifPresentOrElse(
                        factory -> {
                            ApplicationEvent event = (ApplicationEvent) factory.createEvent(request);
                            eventPublisher.publishEvent(event);
                        },
                        () -> {
                            throw new DataValidationException("Unsupported event type: " + request.getType());
                        }
                );
    }
}