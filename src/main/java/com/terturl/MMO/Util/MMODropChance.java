package com.terturl.MMO.Util;

import java.util.SplittableRandom;

import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Math.IntMinMax;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MMODropChance {

	@Getter
	private CustomItem ci;
	
	@Getter
	private IntMinMax amount;
	
	@Getter 
	private Double chance;
	
	public boolean getsItem() {
		SplittableRandom sr = new SplittableRandom();
		Double d = sr.nextDouble(100.01);
		return d <= chance;
	}
	
}