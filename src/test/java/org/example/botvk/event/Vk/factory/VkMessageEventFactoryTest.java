package org.example.botvk.event.Vk.factory;

import com.json.botVk.Message;
import com.json.botVk.VkRequest;
import org.example.botvk.event.Vk.dto.VkMessageEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VkMessageEventFactoryTest {

    private VkMessageEventFactory vkMessageEventFactory;

    @BeforeEach
    void setUp() {
        vkMessageEventFactory = new VkMessageEventFactory();
    }

    @Test
    void supportsSuccessful() {
        String type = "message_new";

        boolean supported = vkMessageEventFactory.supports(type);

        assertTrue(supported);
    }

    @Test
    void supportsFailed() {
        String type = "Test";

        boolean supported = vkMessageEventFactory.supports(type);

        assertFalse(supported);
    }

    @Test
    void createEvent() {
        int userId = 1;
        String text = "Test";

        VkMessageEvent expected = VkMessageEvent.builder()
                .source(new Object())
                .text(text)
                .userId(userId)
                .build();

        Message message = new Message();
        message.setText(text);
        message.setFromId(userId);

        com.json.botVk.Object object = new com.json.botVk.Object();
        object.setMessage(message);

        VkRequest vkRequest = new VkRequest();
        vkRequest.setType("message_new");
        vkRequest.setObject(object);

        VkMessageEvent event = vkMessageEventFactory.createEvent(vkRequest);

        assertNotNull(event);
        assertEquals(expected.getText(), event.getText());
        assertEquals(expected.getUserId(), event.getUserId());
    }
}