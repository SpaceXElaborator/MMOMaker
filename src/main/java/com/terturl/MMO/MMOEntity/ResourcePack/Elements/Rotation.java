package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import lombok.Getter;
import lombok.Setter;

/**
 * Java representation of Minecrafts resource pack Rotation that will be applied
 * to a Cube for presentation in game with rotation
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class Rotation {

	@Getter
	@Setter
	private Double angle;

	@Getter
	@Setter
	private String axis;

	@Getter
	private Double[] origin;

	/**
	 * Set's the Rotations origin to the following X,Y, and Z location
	 * @param x New X
	 * @param y New Y
	 * @param z New Z
	 */
	public void setOrigin(Double x, Double y, Double z) {
		origin = new Double[] { x, y, z };
	}

	/**
	 * Will set the origin to Rotation's Origin plus offset
	 * 
	 * @param offset
	 */
	public void addOrigin(Double... offset) {
		origin[0] = origin[0] + offset[0];
		origin[1] = origin[1] + offset[1];
		origin[2] = origin[2] + offset[2];
	}

	/**
	 * Will shrink the origin of the rotation by the ratio amount provided
	 * 
	 * @param ratio Amount to shrink rotation
	 */
	public void shrinkOrigin(Double ratio) {
		origin[0] = shrink(origin[0], ratio);
		origin[1] = shrink(origin[1], ratio);
		origin[1] = shrink(origin[2], ratio);
	}

	private Double shrink(Double d, Double r) {
		return 8.0D * (1.0D - r) + r * d;
	}

}