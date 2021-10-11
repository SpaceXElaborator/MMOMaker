package com.terturl.MMO.Util.Math;

import java.text.DecimalFormat;
import java.util.Random;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinMax {

	private Double min;
	private Double max;
	
	public Double getRandomNumber() {
		Random r = new Random();
		Double randomNum = min + (max - min) * r.nextDouble();
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(randomNum));
	}
	
}