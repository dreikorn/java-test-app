package com.example;

public class ThreadDelayer {

	public static void delayThread(long delay){
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e){
			/* some evil jerk killed our thread :-( */
		}
	}
}
