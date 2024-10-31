package org.example.botvk.config;

import org.example.botvk.util.BotVkPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${spring.cloud.vault.token}")
    private String token;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(BotVkPath.BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }
}
