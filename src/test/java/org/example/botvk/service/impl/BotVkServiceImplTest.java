package org.example.botvk.service.impl;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.example.botvk.exception.DataValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
class BotVkServiceImplTest {
    private MockWebServer mockWebServer;
    private WebClient webClient;
    private BotVkServiceImpl botVkService;

    private String message;
    private int userId;

    @BeforeEach
    void setUp() throws IOException {
        message = "Test message";
        userId = 1;
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        botVkService = new BotVkServiceImpl(webClient, "5.199");
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void sendMessageOk() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"response\":\"Message sent\"}")
                .addHeader("Content-Type", "application/json"));

        botVkService.sendMessage(userId, message);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertThat(recordedRequest.getPath()).startsWith("/messages.send");
        assertThat(recordedRequest.getPath()).contains("user_id=1");
        assertThat(recordedRequest.getPath()).contains("message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
        assertThat(recordedRequest.getPath()).contains("v=5.199");

        String randomIdParam = recordedRequest.getPath().replaceAll(".*random_id=([0-9]+).*", "$1");
        assertThat(randomIdParam).matches("\\d+");
    }

    @Test
    void sendMessageError() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> capturedError = new AtomicReference<>();

        try {
            botVkService.sendMessage(userId, message);
        } catch (DataValidationException ex) {
            capturedError.set(ex);
            latch.countDown();
        }

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertThat(recordedRequest.getPath()).startsWith("/messages.send");
        assertThat(recordedRequest.getPath()).contains("user_id=1");
        assertThat(recordedRequest.getPath()).contains("message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
        assertThat(recordedRequest.getPath()).contains("v=5.199");

        String randomIdParam = recordedRequest.getPath().replaceAll(".*random_id=([0-9]+).*", "$1");
        assertThat(randomIdParam).matches("\\d+");

    }

}
