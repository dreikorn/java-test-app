package com.example;

import java.util.concurrent.Flow;

import static java.lang.System.out;

public class TestSubscriber<T> implements Flow.Subscriber<T> {

    private Flow.Subscription subscription;
    private String subscriberId;
    private long delay;

    public TestSubscriber(String subscriberId, Long delay){
        this.subscriberId = subscriberId;
        this.delay = delay;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        out.println(subscriberId + " Subscribed");
        subscription.request(1); //a value of  Long.MAX_VALUE may be considered as effectively unbounded
    }

    @Override
    public void onNext(T item) {
        try {
            Thread.sleep(delay);
        }catch (InterruptedException ie){
            out.println(subscriberId + " Interrupted");
        }
        out.println(subscriberId + " Got Item : " + item + " in Thread " + Thread.currentThread().getName());
        subscription.request(1); //a value of  Long.MAX_VALUE may be considered as effectively unbounded
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Done");
    }
}