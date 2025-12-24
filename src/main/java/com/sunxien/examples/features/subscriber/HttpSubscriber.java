package com.sunxien.examples.features.subscriber;

import com.sunxien.examples.entity.Output;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
public class HttpSubscriber implements Subscriber<Output> {


    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Output item) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
