package com.sunxien.examples.controller;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.sunxien.examples.entity.Output;
import com.sunxien.examples.mocker.Mocker;
import com.sunxien.examples.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.UUID;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
@RestController
public class ChatController {

    /**
     * @param question
     * @return String
     */
    @GetMapping(path = "/api/v1/chat/client/sync/{question}")
    public String chatClientSync(@PathVariable(name = "question") String question) {
        log.info("request chat client api sync success");
        try {
            return Mocker.createChatClient()
                    .prompt(question)
                    .call()
                    .content();
        } catch (Throwable th) {
            return "call LLM failed. Error: " + th.getMessage();
        }
    }

    /**
     * @param question
     * @return String
     */
    @GetMapping(path = "/api/v1/chat/client/stream/{question}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Output> chatClientStream(@PathVariable(name = "question") String question) {
        try {
            return Mocker.createChatClient()
                    .prompt(question)
                    .tools(Mocker.createToolCallbacks())
                    .stream()
                    .chatResponse()
                    .filter(Objects::nonNull)
                    .map(r -> {
                        AssistantMessage message = r.getResult().getOutput();
                        String text = message.getText();
                        Object reasoningContent = message.getMetadata().get("reasoningContent");
                        String finishReason = r.getResult().getMetadata().getFinishReason();
                        return new Output("CHAT_CLIENT", StreamUtils.processContent(reasoningContent, text, finishReason));
                    });
        } catch (Throwable th) {
            return Flux.just(new Output("CHAT", "call LLM failed. Error: " + th.getMessage()));
        }
    }

    /**
     * @param question
     * @return String
     */
    @GetMapping(path = "/api/v1/chat/agent/stream/{question}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Output> chatAgentStream(@PathVariable(name = "question") String question) {
        try {
            ReactAgent chatAgent = ReactAgent.builder()
                    .name("CHAT_AGENT")
                    .model(Mocker.createChatModel())
                    .outputType(String.class)
                    .systemPrompt("你是一位百事通。")
                    .instruction("回答用户的问题。")
                    .description("你是一个聊天机器人，陪伴客户聊天，回答用户问题。")
                    .enableLogging(true)
                    .chatOptions(Mocker.createChatOptions())
                    .tools(Mocker.createToolCallbacks())
                    .saver(new MemorySaver()) // TODO Short-Term Memory
                    .build();
            RunnableConfig runnableConfig = RunnableConfig.builder()
                    .threadId(UUID.randomUUID().toString())
                    .build();
            return chatAgent.stream(question, runnableConfig)
                    .filter(e -> {
                        return e instanceof StreamingOutput<?>
                                && ((StreamingOutput<?>) e).message() instanceof AssistantMessage;
                    })
                    .map(r -> {
                        Message message = ((StreamingOutput<?>) r).message();
                        String text = message.getText();
                        Object reasoningContent = message.getMetadata().get("reasoningContent");
                        Object finishReason = message.getMetadata().get("finishReason");
                        return new Output("CHAT", StreamUtils.processContent(reasoningContent, text, finishReason));
                    });
        } catch (Throwable th) {
            return Flux.just(new Output("CHAT", "call LLM failed. Error: " + th.getMessage()));
        }
    }
}
