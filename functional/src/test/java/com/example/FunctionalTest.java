package com.example;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.*;


public class FunctionalTest {
	
	private static long MAX_SIZE_DOUBLE = 10L;
	private static List<Double> randomDoubles = null;

	
	@BeforeClass
	public static void setUp() throws Exception {
		Stream<Double> randomStream = Stream.generate(Math::random).limit(FunctionalTest.MAX_SIZE_DOUBLE);
		randomDoubles = randomStream.collect(Collectors.toList());
	}
	
	@Test
	public void andThenTest() {

		Comparator<Double> highSorter = (p, n)->p>=n?1:-1;

		Double firstValue = andThenTestBase(highSorter, randomDoubles).orElse(null);

		out.println("Testing andThenTest " + firstValue);

		Collections.sort(randomDoubles, highSorter);

		assertEquals(randomDoubles.get(0), firstValue);
	}

	private Optional<Double> andThenTestBase(Comparator<Double> sorterParam, List<Double> doubles) {

		BiFunction<List<Double>, Comparator<Double>, List<Double>> sortElem =
				(longs, sorter) -> longs.stream().sorted(sorter)
						.collect(Collectors.toList());

		Function<List<Double>, Optional<Double>> firstElem =
				l -> l.stream().findFirst();


		BiFunction<List<Double>, Comparator<Double>, Optional<Double>> highest =
				sortElem.andThen(firstElem);

		return highest.apply(doubles,sorterParam);
	}

	@Test
	public void composeTest() {

		Function<List<Double>, List<Double>> sort =
				longs -> longs.stream().sorted()
						.collect(Collectors.toList());

		Function<List<Double>, Optional<Double>> first =
				l -> l.stream().findFirst();

		Function<Optional<Double>, Optional<Double>> negate =
				o -> o.map(d -> -d);


		Function<List<Double>, Optional<Double>> highestNegated = negate.compose(first).compose(sort);

		Double firstValueNegated = highestNegated.apply(randomDoubles).orElse(null);

		out.println("Testing composeTest " + firstValueNegated);

		Collections.sort(randomDoubles);
	}

}
