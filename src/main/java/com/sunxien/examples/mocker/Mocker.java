package com.sunxien.examples.mocker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
public final class Mocker {

    private static final String MODEL_NAME = "";
    private static final String BASE_URL = "";
    private static final String API_KEY = "";
    private static final String DEFAULT_SYSTEM_PROMPT = "你是一个百事通。";

    private Mocker() {
    }

    /**
     * Create ChatClient
     *
     * @return ChatClient
     */
    public static ChatClient createChatClient() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .build();
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(MODEL_NAME)
                .temperature(0.00D)
                .frequencyPenalty(0.00D)
                .presencePenalty(0.00D)
                .extraBody(createExtraBody())
                .build();
        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(openAiChatOptions)
                .build();
        return ChatClient.builder(openAiChatModel)
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .build();
    }

    /**
     * Create OpenAiChatModel
     *
     * @return OpenAiChatModel
     */
    public static OpenAiChatModel createChatModel() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .build();
    }

    /**
     * Create extra HTTP Body
     *
     * @return Map<String, Object>
     */
    private static Map<String, Object> createExtraBody() {
        Map<String, Object> enableThinking = new HashMap<>();
        enableThinking.put("enable_thinking", true);
        Map<String, Object> extraBody = new HashMap<>();
        extraBody.put("chat_template_kwargs", enableThinking);
        return extraBody;
    }
}
