package org.example.botvk.event.Vk.listener;

import org.example.botvk.event.Vk.dto.VkMessageEvent;
import org.example.botvk.service.impl.BotVkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class VkMessageListenerTest {

    @Mock
    private BotVkServiceImpl botVkService;

    @InjectMocks
    private VkMessageListener vkMessageListener;

    @BeforeEach
    void setUp() {
    }

    @Test
    void onVkMessageEventSuccessful() {
        int userId = 123;
        String messageText = "Test";
        VkMessageEvent event = new VkMessageEvent(this, userId, messageText);

        vkMessageListener.onVkMessageEvent(event);

        ArgumentCaptor<Integer> userIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(botVkService, times(1)).sendMessage(userIdCaptor.capture(), messageCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(messageCaptor.getValue()).isEqualTo("Вы написали: Test");
    }
}