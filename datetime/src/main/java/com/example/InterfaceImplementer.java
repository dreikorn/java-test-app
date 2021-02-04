package com.example;

public class InterfaceImplementer implements InterfaceOne, InterfaceTwo {

	@Override
	public int doStepThree() {
		return 3;
	}

	@Override
	public int doStepFour() {
		return 4;
	}

	@Override
	public int doStepOne() {
		return 1;
	}

	@Override
	public int doStepTwo() {
		return 2;
	}
	
	public int doAllSteps() {
		return InterfaceTwo.super.doAllSteps(); //That's how you resolve multiple inheritance Collision
	}

}
