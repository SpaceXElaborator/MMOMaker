package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a texture face of the BBOCube object and what the integer value of
 * the face is from the .bbmodel file
 * 
 * @author Sean Rahman
 * @since 0.55.0
 *
 */
@ToString
public class BBOFace {

	@Getter
	private double[] uv = new double[4];

	@Getter
	@Setter
	private Integer rotation = 0;

	@Getter
	@Setter
	private Integer texture;

	/**
	 * Will set the textures UV levels, most typically, this will be 0,0,0,0.2
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public void setUV(Double a, Double b, Double c, Double d) {
		uv[0] = a;
		uv[1] = b;
		uv[2] = c;
		uv[3] = d;
	}

}