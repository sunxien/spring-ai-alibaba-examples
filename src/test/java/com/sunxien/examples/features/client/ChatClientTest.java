package com.sunxien.examples.features.client;

import com.sunxien.examples.entity.Output;
import com.sunxien.examples.features.subscriber.StdoutSubscriber;
import com.sunxien.examples.mocker.Mocker;
import com.sunxien.examples.utils.StreamUtils;
import org.junit.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
public class ChatClientTest {

    @Test
    public void testChatClient() {
        try {
            String input = """
                    你好，1+1等于几？
                    """;

            Flux<Output> flux = Mocker.createChatClient()
                    .prompt(input)
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

            CountDownLatch latch = new CountDownLatch(1);
            flux.subscribe(new StdoutSubscriber(latch));
            latch.await();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
