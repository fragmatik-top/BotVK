package org.example.botvk.config;

import org.example.botvk.until.BotVkPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Base64;

@Configuration
public class WebClientConfig {
    private String token = "vk1.a.0LTMUOT7_OSsNJWky5m9R_OS401GNEmJHjiPW4fyfFADPvS1tnHC71Zo3AxClG0pwFgwnn94Br3hGy-COqKCWcXjBdo4VmeCiyt3GYIyZ2zm2hb3w3ABruTAGAkExzExAnpIcHLB9W79v5Y85zwDxo9RFiULV9KqOGMmwxcaoyl-6OBgiEr_t2Hi44FcFgDdSLBlBvinqEO96GcBruHtWw";

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(BotVkPath.BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }
}
