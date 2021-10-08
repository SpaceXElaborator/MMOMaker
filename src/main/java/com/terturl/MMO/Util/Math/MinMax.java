package com.terturl.MMO.Util.Math;

import java.text.DecimalFormat;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MinMax {

	@Getter
	private Double min;
	
	@Getter
	private Double max;
	
	public Double getRandomNumber() {
		Random r = new Random();
		Double randomNum = min + (max - min) * r.nextDouble();
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(randomNum));
	}
	
}