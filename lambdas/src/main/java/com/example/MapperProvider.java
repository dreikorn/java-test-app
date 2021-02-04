package com.example;

import java.util.function.Function;

public class MapperProvider {
	
	public static Function<String, Integer> mapToCharIndex(String chr){
		return k -> k.toLowerCase().indexOf(chr);
	}
	
	public static Function<String, String> mapToUpperCase(){
		return k -> k.toUpperCase();
	}
	
}
