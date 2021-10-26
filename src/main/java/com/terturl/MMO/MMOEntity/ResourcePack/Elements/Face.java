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
	private Double[] uv;

	@Getter
	@Setter
	private Integer rotation = 0;

	@Getter
	@Setter
	private String texture;

	@Getter
	@Setter
	private Integer tintindex = 0;

	public void setUv(Double a, Double b, Double c, Double d) {
		uv = new Double[] { a, b, c, d };
	}

}