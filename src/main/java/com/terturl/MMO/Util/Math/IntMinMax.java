package com.terturl.MMO.Util.Math;

import java.util.SplittableRandom;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IntMinMax {

	private Integer min;
	private Integer max;
	
	public Integer getAmount() {
		SplittableRandom sr = new SplittableRandom();
		Integer i = sr.nextInt(min, max+1);
		return i;
	}
	
}