package org.example.botvk.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.botvk.exception.DataValidationException;
import org.example.botvk.until.BotVkPath;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotVkServiceImpl {
    private final WebClient webClient;

    public void sendMessage(int userId, String message) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path(BotVkPath.MESSAGES_SEND_PATH)
                        .queryParam("user_id", userId)
                        .queryParam("random_id", System.currentTimeMillis())
                        .queryParam("message", message)
                        .queryParam("v", "5.199")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .subscribe(response -> {
                            log.info("Сообщение отправлено: {}", response);
                        },
                        error -> {
                            log.error("Error: {}", error);
                            throw new DataValidationException("Error: " + error.getMessage());
                        });
    }
}
