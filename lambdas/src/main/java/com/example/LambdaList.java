package com.example;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;
	
	public <R> LambdaList<R> customMapAndCopy(Function<E,R> mapExpr){
		
		LambdaList<R> mapResult = new LambdaList<>();
		for(E listElement : this){
			R mappedListElement = mapExpr.apply(listElement);
			mapResult.add(mappedListElement);
		}
		
		return mapResult;
	}
	
	public <T, R> LambdaList<R> customBiMapAndCopy(BiFunction<E,T,R> mapExpr, T inputVal){
		
		LambdaList<R> mapResult = new LambdaList<>();
		for(E listElement : this){
			R mappedListElement = mapExpr.apply(listElement, inputVal);
			mapResult.add(mappedListElement);
		}
		return mapResult;
	}

	public <R> LambdaList<R> customDumbMapAndCopy(Supplier<R> mapExpr){
		
		LambdaList<R> mapResult = new LambdaList<>();
		for(E listElement : this){
			R mappedListElement = mapExpr.get();
			mapResult.add(mappedListElement);
		}
		return mapResult;
	}
	
	public <R, Y, Z> LambdaList<R> customWeirdMapAndCopy(WeirdStuffExecutor<E, Y, Z, R> mapExpr, Y inputVal1, Z inputVal2){
		
		LambdaList<R> mapResult = new LambdaList<>();
		for(E listElement : this){
			R mappedListElement = mapExpr.doWeirdStuff(listElement, inputVal1, inputVal2);
			mapResult.add(mappedListElement);
		}
		return mapResult;
	}
	
	public boolean booleanReduce(Predicate<E> evalExpr, boolean unitVal, BiPredicate<Boolean, Boolean> conjunctExpr){
		
		boolean reduceResult = unitVal;
		for(E listElement : this){
			reduceResult = conjunctExpr.test(reduceResult, evalExpr.test(listElement));
		}
		return reduceResult;
	}
	
	public <R> LambdaList<R> customMapFunctionParameter(Function<E, R> mapperFunction){
		
		LambdaList<R> mapResult = new LambdaList<>();
		for(E listElement : this){
			R mappedListElement = mapperFunction.apply(listElement);
			mapResult.add(mappedListElement);
		}
		return mapResult;
	}
	
}
