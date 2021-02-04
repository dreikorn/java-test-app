package com.example;

@FunctionalInterface
public interface WeirdStuffExecutor<X,Y,Z,R> {

	public R doWeirdStuff(X param1, Y param2, Z param3);
}
