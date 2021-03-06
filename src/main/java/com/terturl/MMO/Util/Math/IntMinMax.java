package com.terturl.MMO.Util.Math;

import java.util.SplittableRandom;

import lombok.AllArgsConstructor;

/**
 * A math helper class to get a random Integer between the min and max values
 * @author Sean Rahman
 * @since 0.47.0
 * 
 */
@AllArgsConstructor
public class IntMinMax {

	private int min;
	private int max;
	
	/**
	 * Will get the random Integer value between two integers and return the value
	 * @return random integer
	 */
	public int getAmount() {
		SplittableRandom sr = new SplittableRandom();
		int i = sr.nextInt(min, max+1);
		return i;
	}
	
}