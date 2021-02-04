package com.example;

import static java.lang.System.out;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class ConcurrTest
{

	private static long TH_TIMEOUT = 1_000L;
	private static long TH_SLEEP = 10L;
	
    @Test
    public void lambdaRunnableAndAtomic() throws InterruptedException {

    	final AtomicInteger atInt = new AtomicInteger(0);
        Runnable taskJava8 = () -> {
     	   atInt.incrementAndGet();
     	  ThreadDelayer.delayThread(ConcurrTest.TH_SLEEP);
        };
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for(int idx = 0; idx < 10; idx++){
            executor.execute(taskJava8);
        }

        executor.shutdown(); while (!executor.isTerminated()) {Thread.sleep(10);}
    	
    	out.println("Testing lambdaRunnable: " + atInt.get());
		assertEquals(atInt.get(),10);
    }
    
    @Test
    public void parallelArray() throws InterruptedException {

    	Long testArr[] = new Long[30]; //you perhaps would not parallelize a Sort of only 30 Elements
    	Arrays.parallelSetAll(testArr, i -> 30L-i);
    	out.println("Testing parallelArray first Element unsorted: " + testArr[0]);
    	Arrays.parallelSort(testArr);    	
    	out.println("Testing parallelArray first Element sorted: " + testArr[0]);
    	long lastElem = testArr[0];
    	assertEquals(lastElem, 1L);
    }
    
    @Test
    public void completableFutureSupplying() throws InterruptedException, ExecutionException {
    	
    	Long testArr[] = new Long[30];
    	Arrays.parallelSetAll(testArr, i -> 30L-i);
    	
    	final CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
    		ThreadDelayer.delayThread(ConcurrTest.TH_SLEEP);
    		out.println(Thread.currentThread().getName());
    		return "this";
    	});
    	//You can put an Accept here because the first CompletableFuture supplies a result
    	future.thenAccept(r -> {
    		out.println(Thread.currentThread().getName());
    		out.println("Testing completableFutureSupplying: Step Two got: " + r);});
    	
    	String result = future.get();
    	
    	out.println("Testing completableFutureSupplying Result: " + result);
		assertEquals("this",result);
    }
    
    @Test
    public void completableFutureChain() throws InterruptedException, ExecutionException {
    	
    	final CompletableFuture<Void> future = CompletableFuture.
    			runAsync(()->{ThreadDelayer.delayThread(ConcurrTest.TH_SLEEP);out.println("Testing completableFutureChain: Step 1");}).
    			thenRunAsync(()->{ThreadDelayer.delayThread(ConcurrTest.TH_SLEEP);out.println("Testing completableFutureChain: Step 2");}).
    			thenRunAsync(()->{ThreadDelayer.delayThread(ConcurrTest.TH_SLEEP);out.println("Testing completableFutureChain: Step 3");}).
    			thenRunAsync(()->{ThreadDelayer.delayThread(ConcurrTest.TH_SLEEP);out.println("Testing completableFutureChain: Step 4");});
    	out.println("Testing completableFutureChain: main thread");
    	future.get();
		assertEquals(0,0);
    }
}
