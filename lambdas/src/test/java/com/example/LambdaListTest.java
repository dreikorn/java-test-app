package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.lang.System.out;

import junit.framework.TestCase;

public class LambdaListTest extends TestCase {
	
	private LambdaList<String> testObjFactory(){
		LambdaList<String> testObj = new LambdaList<>();
		//Taking Advantage of Closable Interface of FileReader BufferedReader
		try(FileReader flReader = new FileReader(LambdaListTest.class.getResource("/names.properties").getFile());
			BufferedReader bfReader = new BufferedReader(flReader);){
			Properties nameProps = new Properties();
			nameProps.load(bfReader);
			nameProps.keySet().forEach((k)->testObj.add((String)k));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Cannot load props");
		}
		return testObj;
	}

	public void testCustomMapAndCopy1() {
		LambdaList<String> testInputObj = this.testObjFactory();
		
		LambdaList<Integer> testOutputObj = 
				testInputObj.customMapAndCopy(
						//Here we pass the mapping Function
						(listElem)->{return listElem.toLowerCase().lastIndexOf("n");}
				);
		
		
		out.println("");
		out.print("Testing testCustomMapAndCopy1: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		//Summing up List Members with collect
		assertTrue(testOutputObj.stream().collect(Collectors.summingLong(v -> v)) == 9L);

	}
	
	public void testCustomMapAndCopy2() {
		LambdaList<String> testInputObj = this.testObjFactory();
		
		String searchVal = "n"; //Here I define a Closure Variable, final is optional but better Style, leaving it out for demonstration below
		
		LambdaList<Integer> testOutputObj = 
				testInputObj.customMapAndCopy(
						//Here we pass the mapping Function
						(listElem)->{return listElem.toLowerCase().lastIndexOf(searchVal);}
				);
		
		//Uncommenting this will break the closure
		//searchVal = "k";
		
		out.println("");
		out.print("Testing testCustomMapAndCopy2: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		//Summing up List Members with parallelStream and Closure
		final AtomicLong sum = new AtomicLong();
		testOutputObj.parallelStream().forEach(sum::addAndGet); //Method Reference
		assertTrue(sum.longValue() == 9L);

	}

	public void testCustomBiMapAndCopy() {
		LambdaList<String> testInputObj = this.testObjFactory();		
		LambdaList<Integer> testOutputObj = 
				testInputObj.customBiMapAndCopy(
						//Here we pass the mapping Function
						(listElem, passedVal)->{return listElem.toLowerCase().lastIndexOf(passedVal);},
						"n"
				);
		
		
		out.println("");
		out.print("Testing testCustomBiMapAndCopy: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		assertTrue(testOutputObj.stream().collect(Collectors.summingLong(v -> v)) == 9L);
	}

	public void testCustomDumbMapAndCopy() {
		LambdaList<String> testInputObj = this.testObjFactory();		
		LambdaList<Long> testOutputObj = 
				testInputObj.customDumbMapAndCopy(
						//Here we pass the mapping Function
						()->{return 1L;}
				);
		
		
		out.println("");
		out.print("Testing testCustomDumbMapAndCopy: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		//Summing up List Members with built in sum
		assertTrue(testOutputObj.stream().mapToLong(k -> k).sum() == 6L);
	}
	
	public void testCustomWeirdMapAndCopy() {
		LambdaList<String> testInputObj = this.testObjFactory();		
		LambdaList<Integer> testOutputObj = 
				testInputObj.customWeirdMapAndCopy(
						//Here we pass the mapping Function
						(listElem, passedVal1, passedVal2)->{return listElem.toLowerCase().substring(0, passedVal2).lastIndexOf(passedVal1);},
						"n",
						3
				);
		
		
		out.println("");
		out.print("Testing testCustomWeirdMapAndCopy: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		assertTrue(testOutputObj.stream().collect(Collectors.summingLong(v -> v)) == -4L);
	}
	
	public void testCustomReduce1() {
		LambdaList<String> testInputObj = this.testObjFactory();		
		
		boolean result = testInputObj.booleanReduce(
						(k) -> {return k.toLowerCase().contains("n");}, //here the return is implicit
						true, 
						(cond1, cond2) -> {return cond1 && cond2;} //here you have to do explicitly due to usage of {}
				);
		out.println("");
		out.print("Testing testCustomReduce1: " + result);
		assertFalse(result);
	}

	public void testCustomReduce2() {
		LambdaList<String> testInputObj = this.testObjFactory();		
		
		boolean result = testInputObj.booleanReduce(
						(k) -> k.toLowerCase().contains("n"), //here the return is implicit
						false, 
						(cond1, cond2) -> {return cond1 || cond2;} //here you have to do explicitly due to usage of {}
				);
		out.println("");
		out.print("Testing testCustomReduce2: " + result);
		assertTrue(result);
	}

	public void testCustomMapFunctionParameter1() {
		LambdaList<String> testInputObj = this.testObjFactory();

		final String chr = "n";
		LambdaList<Integer> testOutputObj = 
				testInputObj.customMapFunctionParameter(
						MapperProvider.mapToCharIndex(chr) //Closure in Action
				);
		
		out.println("");
		out.print("Testing testCustomMapFunctionParameter1: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		assertTrue(testOutputObj.stream().collect(Collectors.summingLong(v -> v)) == 9L);
	}
	
	public void testCustomMapFunctionParameter2() {
		LambdaList<String> testInputObj = this.testObjFactory();
		
		LambdaList<String> testOutputObj = 
				testInputObj.customMapFunctionParameter(
						MapperProvider.mapToUpperCase()
				);
		
		out.println("");
		out.print("Testing testCustomMapFunctionParameter2: ");
		testOutputObj.forEach(v -> out.print(v + " "));
		assertTrue(testOutputObj.stream().filter(k -> k.matches("[a-z]+")).count() == 0L);
	}
	
}
