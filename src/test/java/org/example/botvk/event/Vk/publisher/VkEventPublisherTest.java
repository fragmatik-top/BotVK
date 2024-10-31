package org.example.botvk.event.Vk.publisher;

import com.json.botVk.VkRequest;
import org.example.botvk.event.Vk.dto.VkMessageEvent;
import org.example.botvk.event.Vk.factory.VkEventFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class VkEventPublisherTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private VkEventFactory<VkMessageEvent> eventFactory;

    @InjectMocks
    private VkEventPublisher vkEventPublisher;

    private VkRequest vkRequest;
    private VkMessageEvent vkMessageEvent;

    @BeforeEach
    void setUp() {
        vkMessageEvent = mock(VkMessageEvent.class);
        vkRequest = new VkRequest();
        vkRequest.setType("test");

        vkEventPublisher = new VkEventPublisher(List.of(eventFactory), eventPublisher);
    }

    @Test
    void publishEventSuccessful() throws InterruptedException {

        when(eventFactory.supports(vkRequest.getType())).thenReturn(true);
        when(eventFactory.createEvent(vkRequest)).thenReturn(vkMessageEvent);


        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocationOnMock -> {
            latch.countDown();
            return null;
        }).when(eventPublisher).publishEvent(vkMessageEvent);

        vkEventPublisher.publishEvent(vkRequest);

        assertTrue(latch.await(2, TimeUnit.SECONDS));

        verify(eventFactory, timeout(1)).supports(vkRequest.getType());
        verify(eventFactory, timeout(1)).createEvent(vkRequest);
        verify(eventPublisher, timeout(1)).publishEvent(vkMessageEvent);
    }

    @Test
    void publishEventFailed() {
        when(eventFactory.supports(vkRequest.getType())).thenReturn(false);

        vkEventPublisher.publishEvent(vkRequest);

        verify(eventPublisher, never()).publishEvent(vkMessageEvent);
    }
}