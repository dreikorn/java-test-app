package com.example;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

import static java.lang.System.out;

public class TestTransformProcessor<T,R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

    private Function function;
    private Flow.Subscription subscription;

    public TestTransformProcessor(Function<? super T, ? extends R> function) {
        super();
        this.function = function;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        out.println("TestTransformProcessor Subscribed");
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        submit((R) function.apply(item));
        out.println("TestTransformProcessor Got Item : " + item + " in Thread " + Thread.currentThread().getName());
        subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        close();
    }
}