package com.terturl.MMO.Entity;

import java.util.SplittableRandom;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MMOEntityDrop {
	
	@Getter
	private CustomItem ci;
	
	@Getter
	private Integer amount;
	
	@Getter 
	private Double chance;
	
	public boolean getsItem() {
		SplittableRandom sr = new SplittableRandom();
		Double d = sr.nextDouble(100.01);
		return d <= chance;
	}
	
}