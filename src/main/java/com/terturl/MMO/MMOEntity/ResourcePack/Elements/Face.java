package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import lombok.Getter;
import lombok.Setter;

/**
 * Java representation of Minecrafts resource pack Face that will be applied to
 * a Cube for presentation in game
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class Face {

	@Getter
	private double[] uv;

	@Getter
	@Setter
	private int rotation = 0;

	@Getter
	@Setter
	private String texture;

	@Getter
	@Setter
	private int tintindex = 0;

	public void setUv(double a, double b, double c, double d) {
		uv = new double[] { a, b, c, d };
	}

}