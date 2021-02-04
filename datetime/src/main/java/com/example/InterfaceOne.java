package com.example;

public interface InterfaceOne {

	public int doStepOne();
	
	public int doStepTwo();
	
	public default int doAllSteps(){
		return this.doStepOne() + this.doStepTwo();
	}
}
