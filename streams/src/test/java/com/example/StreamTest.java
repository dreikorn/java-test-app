package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.*;


public class StreamTest {
	
	private static long MAX_SIZE_DOUBLE = 10L;
	private static long MAX_SIZE_LONG = 100_000L;
	private static List<Double> randomDoubles = null;
	private static List<Long> increasingLongs = null;

	
	@BeforeClass
	public static void setUp() throws Exception {
		Stream<Double> randomStream = Stream.generate(Math::random).limit(StreamTest.MAX_SIZE_DOUBLE);
		randomDoubles = randomStream.collect(Collectors.toList());
		Stream<Long> longStream = Stream.iterate(0L, v -> v+1L).limit(StreamTest.MAX_SIZE_LONG);
		increasingLongs = longStream.collect(Collectors.toList());
	}
	
	@Test
	public void sequentialMap() {
		
		randomDoubles.stream().mapToDouble(v -> v).forEach(v -> {try {Thread.sleep(10);} catch (InterruptedException e) {/*wont happen here*/}});;
		out.println("Testing sequentialMap");
		assertEquals(0, 0);
	}
	
	@Test
	public void parallelMap() {
		
		//As we see here, creating a Stream does not change the underlying Object (List in this case) itself
		randomDoubles.stream().parallel().unordered().forEach(v -> {try {Thread.sleep(10);} catch (InterruptedException e) {/*wont happen here*/}});
		out.println("Testing parallelMap");
		assertEquals(0, 0);
	}

	@Test
	public void sequentialConcatReduce() {

		//combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)
		String sumResult =
				Stream
					.of(10,11,12,13,14,15)
					.map(elem->String.format("%02X",elem))
					.reduce(new StringBuilder(), (p,n)->p.append(n), (p,n)-> p) //the combiner makes no HERE difference as StringBuilder is mutable and returns this
					.toString();

		out.println("Testing sequentialConcatReduce: " + sumResult);
	}

	@Test
	public void sequentialAddCorrect() {
		/* In real life you would rather use the reduce() Method than doing a sum like this */
		final AtomicLong sumResult = new AtomicLong(0L);
		increasingLongs.forEach(v -> sumResult.addAndGet(v));
		out.println("Testing sequentialAddCorrect: " + sumResult);
		assertEquals(sumResult.longValue(), 4999950000L);
	}

	@Test
	public void sequentialAddReduce() {
		Long sumResult = increasingLongs.stream().reduce(0L, (p,n)->p+n);
		out.println("Testing sequentialAddReduce: " + sumResult);
		assertEquals(sumResult.longValue(), 4999950000L);
	}

	@Test
	public void parallelAddWrong() {
		
		final Long[] sumResult = new Long[]{0L}; //Provoking a Race Condition
		increasingLongs.parallelStream().unordered().forEach(v -> sumResult[0] = sumResult[0]+v);
		out.println("Testing parallelAddWrong: " + sumResult[0]);
		assertFalse(sumResult[0]==4999950000L);
	}
	
	@Test
	public void parallelAddCorrect() {
		
		final AtomicLong sumResult = new AtomicLong(0L);
		increasingLongs.parallelStream().unordered().forEach(v -> sumResult.addAndGet(v));
		out.println("Testing parallelAddCorrect: " + sumResult);
		assertEquals(sumResult.longValue(), 4999950000L);
	}
	
	@Test
	public void playingWithOptional() {
		
		Optional<Double> firstElem = randomDoubles.stream().findFirst();
		if(firstElem.isPresent()){
			//This is how NOT to work with optionals
			out.println("Testing playingWithOptional The wrong way: " + firstElem.get());
		}
		firstElem.ifPresent((d)-> out.println("Testing playingWithOptional The right way: " + d));
		
		Optional<String> optStr = Optional.ofNullable(null);
		String resultStr = optStr.orElse("Other Option"); // You will always get a Value out of this
		assertEquals(resultStr, "Other Option");
	}

}
