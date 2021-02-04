package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.lang.System.out;

import junit.framework.TestCase;

public class MethRefTest extends TestCase {
	
	private Map<StringBuilder,String> testMapFactory(){
		
		Map<StringBuilder,String> testMap = new HashMap<>(); //A REALLY BAD idea to use an mutable as Key but needed to play all scenarios 
		//Taking Advantage of Closable Interface of FileReader BufferedReader
		try(FileReader flReader = new FileReader(MethRefTest.class.getResource("/names.properties").getFile());
			BufferedReader bfReader = new BufferedReader(flReader);){
			Properties nameProps = new Properties();
			nameProps.load(bfReader);
			nameProps.entrySet().forEach((e)->{testMap.put(new StringBuilder((String)e.getKey()), (String)e.getValue());});
		} catch (IOException e) {
			e.printStackTrace();
			fail("Cannot load props");
		}
		return testMap;
	}
	
	private List<String> testListFactory(){
		
		//you also can use Constructors as Method References
		List<String> retList = this.testMapFactory().keySet().stream().map(String::new).collect(Collectors.toList()); //METHREF
		return retList;
	}

	public void testObjMethod(){
		
		List<String> testInputObj = this.testListFactory();
		final StringBuilder replVal = new StringBuilder(); //final but not sooo final, NO concurrency with THIS code!
		testInputObj.forEach(replVal::append); //METHREF
		out.println("Testing testObjMethod: " + replVal.toString());
		assertTrue(replVal.length() == 39);
	}
	
	public void testClassInstMethod(){
		
		Map<StringBuilder,String> testInputObj = this.testMapFactory();
		testInputObj.forEach(StringBuilder::append); //METHREF
		String keyString = testInputObj.keySet().stream().collect(Collectors.joining(" "));
		out.println("Testing testClassInstMethod: " + keyString);
		assertTrue(keyString.length() == 113);
	}
	
	public void testClassStatMethod(){
		
		List<Integer> testInputObj = this.testListFactory().stream().map(String::length).collect(Collectors.toList());
		List<String> testOutputObj = testInputObj.stream().map(String::valueOf).collect(Collectors.toList()); //METHREF
		out.println("Testing testClassStatMethod: ");
		testOutputObj.forEach(v -> out.println(v + " "));
		assertTrue(testOutputObj.stream().filter(k -> k.matches("[0-9]+")).count() == 6L);
	}
}
