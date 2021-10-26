package com.terturl.MMO.Entity;

import java.util.SplittableRandom;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Call that holds the chances of a CustomItem dropping from an MMOEntity
 * 
 * @see com.terturl.MMO.Entity.MMOEntity
 * @author Sean Rahman
 * @since 0.37.0
 *
 */
@AllArgsConstructor
public class MMOEntityDrop {

	@Getter
	private CustomItem ci;

	@Getter
	private Integer amount;

	@Getter
	private Double chance;

	/**
	 * Creates a new double from the SplittableRandom class and compares it to the
	 * chance value to see if the player gets the item or not
	 * 
	 * @return If the player gets the item or not
	 */
	public boolean getsItem() {
		SplittableRandom sr = new SplittableRandom();
		Double d = sr.nextDouble(100.01);
		return d <= chance;
	}

}