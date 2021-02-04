package com.example;

public interface InterfaceTwo {

	public int doStepThree();
	
	public int doStepFour();
	
	public default int doAllSteps(){
		return this.doStepThree() + this.doStepFour();
	}
}
