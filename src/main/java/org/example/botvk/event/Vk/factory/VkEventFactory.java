package org.example.botvk.event.Vk.factory;

import com.json.botVk.VkRequest;

public interface VkEventFactory<T> {
    boolean supports(String type);

    T createEvent(VkRequest request);
}
