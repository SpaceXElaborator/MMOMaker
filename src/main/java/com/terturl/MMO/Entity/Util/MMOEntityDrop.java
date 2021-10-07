package com.terturl.MMO.Entity.Util;

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
	private Integer chance;
	
	public boolean getsItem() {
		SplittableRandom sr = new SplittableRandom();
		Integer i = sr.nextInt(101);
		return i <= chance;
	}
	
}