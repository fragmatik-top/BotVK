package org.example.botvk.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.botvk.exception.DataValidationException;
import org.example.botvk.service.BotVkService;
import org.example.botvk.util.BotVkPath;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotVkServiceImpl implements BotVkService {

    private final WebClient webClient;
    private final String apiVersion;

    @Override
    public void sendMessage(int userId, String message) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path(BotVkPath.MESSAGES_SEND_PATH)
                        .queryParam("user_id", userId)
                        .queryParam("random_id", System.currentTimeMillis())
                        .queryParam("message", URLEncoder.encode(message, StandardCharsets.UTF_8))
                        .queryParam("v", apiVersion)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .subscribe(response -> {
                            log.info("The message has been sent: {}", response);
                        },
                        error -> {
                            log.error("Error: {}", error);
                            throw new DataValidationException("Error: " + error.getMessage());
                        });
    }
}
