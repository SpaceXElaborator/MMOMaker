package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import lombok.Getter;

/**
 * Java representation of Minecrafts resource pack Display that will be applied to
 * a model for representation in game
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class Display {

	@Getter
	private Double[] translation = new Double[] {0.0D, -6.4D, 0.0D};
	
	@Getter
	private Double[] rotation = new Double[] {0.0D, 0.0D, 0.0D};
	
	@Getter
	private Double[] scale = new Double[3];
	
	/**
	 * Will move the Display location to a new location
	 * @param offset Offset to move by
	 */
	public void moveTranslation(Double... offset) {
		translation[0] = translation[0] - offset[0] * scale[0];
		translation[1] = translation[1] - offset[1] * scale[1];
		translation[2] = translation[2] - offset[2] * scale[2];
	}
	
	/**
	 * Manually set a Display's location
	 * @param x New X
	 * @param y New Y
	 * @param z New Z
	 */
	public void setTranslation(Double x, Double y, Double z) {
		translation[0] = x;
		translation[1] = y;
		translation[2] = z;
	}
	
	/**
	 * Manually set a Display's rotation
	 * @param x New RX
	 * @param y New RY
	 * @param z Mew RZ
	 */
	public void setRotation(Double x, Double y, Double z) {
		rotation[0] = x;
		rotation[1] = y;
		rotation[2] = z;
	}
	
	/**
	 * Manually set a Display's scale
	 * @param x New SX
	 * @param y New SY
	 * @param z Mew SZ
	 */
	public void setScale(Double x, Double y, Double z) {
		scale[0] = x;
		scale[1] = y;
		scale[2] = z;
	}
	
}