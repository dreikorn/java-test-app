package com.example;

import java.util.Arrays;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamTest {

    public static void main(String[] args) throws InterruptedException{

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        //Create Processor and Subscriber
        TestTransformProcessor<String, Integer> transformProcessor = new TestTransformProcessor<>(s -> Integer.parseInt(s)+1);
        TestSubscriber<Integer> subscriber1 = new TestSubscriber<>("test1", 10L);
        TestSubscriber<Integer> subscriber2 = new TestSubscriber<>("test2", 40L);

        //Chain Processor and Subscriber
        publisher.subscribe(transformProcessor);
        transformProcessor.subscribe(subscriber1);
        transformProcessor.subscribe(subscriber2);

        System.out.println("Publishing Items from " + Thread.currentThread().getName());
        String[] items = {"1", "2", "3", "4", "5", "6"};
        Arrays.asList(items).stream().forEach(i -> publisher.submit(i));
        Thread.sleep(2000); //Needed to avoid Main Thread ending before Stream has finished
        publisher.close();

    }

}
