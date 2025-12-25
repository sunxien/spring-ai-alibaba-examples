package com.sunxien.examples.features.singleagent;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.sunxien.examples.entity.Output;
import com.sunxien.examples.features.subscriber.StdoutSubscriber;
import com.sunxien.examples.mocker.Mocker;
import com.sunxien.examples.utils.StreamUtils;
import org.junit.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
public class SingleAgentTest {

    @Test
    public void testSingleAgent() {
        try {

            String input = """
                你好，1+1等于几？
                """;

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

            Flux<Output> flux = chatAgent.stream(input, runnableConfig)
                    .filter(e -> {
                        return e instanceof StreamingOutput<?>
                                && ((StreamingOutput<?>) e).message() instanceof AssistantMessage;
                    }).map(r -> {
                        Message message = ((StreamingOutput<?>) r).message();
                        String text = message.getText();
                        Object reasoningContent = message.getMetadata().get("reasoningContent");
                        Object finishReason = message.getMetadata().get("finishReason");
                        return new Output("CHAT", StreamUtils.processContent(reasoningContent, text, finishReason));
                    });

            CountDownLatch latch = new CountDownLatch(1);
            flux.subscribe(new StdoutSubscriber(latch));
            latch.await();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
