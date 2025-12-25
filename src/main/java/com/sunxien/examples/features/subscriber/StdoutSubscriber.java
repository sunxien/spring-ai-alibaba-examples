package com.sunxien.examples.features.subscriber;

import com.google.common.base.Strings;
import com.sunxien.examples.entity.Output;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CountDownLatch;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
public class StdoutSubscriber implements Subscriber<Output> {

    private boolean inThinking = false;
    private final CountDownLatch latch;

    public StdoutSubscriber(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Output output) {
        String content = output.getContent();
        if (content.contains("<think>")) {
            this.inThinking = true;
            System.out.println("思考中....");
            content = content.replace("<think>", "");
        }
        System.out.print(content);
        if (content.contains("</think>")) {
            this.inThinking = false;
        }
    }

    @Override
    public void onError(Throwable th) {
        System.err.println("\n❌错误：" + th.getMessage());
        this.latch.countDown();
    }

    @Override
    public void onComplete() {
        System.out.println();
        System.out.println(Strings.repeat("-", 80));
        System.out.println("* 以上回答来自大模型，仅供参考。");
        this.latch.countDown();
    }
}
