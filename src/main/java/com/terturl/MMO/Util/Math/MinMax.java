package com.terturl.MMO.Util.Math;

import java.text.DecimalFormat;
import java.util.Random;

import lombok.AllArgsConstructor;

/**
 * A math helper class to get a random double between the min and max values
 * @author Sean Rahman
 * @since 0.37.0
 * 
 */
@AllArgsConstructor
public class MinMax {

	private Double min;
	private Double max;
	
	/**
	 * Will get the random double value between two doubles and return a value to the second decimal place
	 * @return formatted double value to the second decimal place
	 */
	public Double getRandomNumber() {
		Random r = new Random();
		Double randomNum = min + (max - min) * r.nextDouble();
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(randomNum));
	}
	
}