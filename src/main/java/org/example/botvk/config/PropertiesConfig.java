package org.example.botvk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "bot.vk")
public class PropertiesConfig {
    private String apiVersion;
    private String code;

    @Bean
    public String apiVersion() {
        return apiVersion;
    }

    @Bean
    public String code() {
        return code;
    }
}

