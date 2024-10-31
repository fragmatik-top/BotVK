package org.example.botvk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.botVk.VkRequest;
import org.example.botvk.event.Vk.publisher.VkEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BotVkController.class)
@Import(BotVkControllerTest.Config.class)
class BotVkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VkEventPublisher eventPublisher;

    private VkRequest vkRequest;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        vkRequest = new VkRequest();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Проверка на потверждение")
    void handlerEventReturnConfirmationCode() throws Exception {

        vkRequest.setType("confirmation");

        ResultActions result = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vkRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().string("test_code"));
    }

    @Test
    @DisplayName("Проверка обработки событий")
    void handlerEventHandlesOtherTypes() throws Exception {

        vkRequest.setType("message_new");

        ResultActions result = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vkRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(eventPublisher, times(1)).publishEvent(vkRequest);
    }

    @TestConfiguration
    static class Config {
        @Bean
        public String code() {
            return "test_code";
        }
    }
}