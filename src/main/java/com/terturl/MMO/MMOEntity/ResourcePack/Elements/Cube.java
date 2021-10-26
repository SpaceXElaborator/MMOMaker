package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Java representation of Minecrafts resource pack Cube for representation in
 * game
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class Cube {

	@Getter
	@Setter
	private String name;

	@Getter
	private Double[] from;

	@Getter
	private Double[] to;

	@Getter
	@Setter
	private Rotation rotation;

	@Getter
	private Map<String, Face> faces = new HashMap<>();

	/**
	 * Set the uppermost left corner of the cube
	 * @param x New X
	 * @param y New Y
	 * @param z New Z
	 */
	public void setFrom(Double x, Double y, Double z) {
		from = new Double[] { x, y, z };
	}

	/**
	 * Set the lowermost right corner of the cube
	 * @param x New X
	 * @param y New Y
	 * @param z New Z
	 */
	public void setTo(Double x, Double y, Double z) {
		to = new Double[] { x, y, z };
	}

	/**
	 * Add a face that will display the texture of the given Face
	 * @param s Name of Face
	 * @param f Face to set
	 */
	public void addFace(String s, Face f) {
		faces.put(s, f);
	}

	/**
	 * Add the offset to the uppermost left corner of the cube
	 * @param offset Offset to add
	 * @return If the cube is within bounds for Minecraft
	 */
	public boolean addFrom(Double... offset) {
		from[0] = from[0] + offset[0];
		from[1] = from[1] + offset[1];
		from[2] = from[2] + offset[2];

		// If this is false, this will need to fail later to prevent glitches from happening
		return (from[0] <= 32.0D && from[0] >= -16.0D && from[1] <= 32.0D && from[1] >= -16.0D && from[2] <= 32.0D
				&& from[2] >= -16.0D);
	}

	/**
	 * Add the offset to the lowermost right corner of the cube
	 * @param offset Offset to add
	 * @return If the cube is within bounds for Minecraft
	 */
	public boolean addTo(Double... offset) {
		to[0] = to[0] + offset[0];
		to[1] = to[1] + offset[1];
		to[2] = to[2] + offset[2];

		// If this is false, this will need to fail later to prevent glitches from happening
		return (to[0] <= 32.0D && to[0] >= -16.0D && to[1] <= 32.0D && to[1] >= -16.0D && to[2] <= 32.0D
				&& to[2] >= -16.0D);
	}

	/**
	 * Shrinks both the to and from of the Cube by the ratio amount given
	 * @param ratio Ratio to shrink by
	 */
	public void shrinkCube(Double ratio) {
		from[0] = shrink(from[0], ratio);
		from[1] = shrink(from[1], ratio);
		from[2] = shrink(from[2], ratio);
		to[0] = shrink(to[0], ratio);
		to[1] = shrink(to[1], ratio);
		to[2] = shrink(to[2], ratio);
	}

	private Double shrink(Double d, Double r) {
		return 8.0D * (1.0D - r) + r * d;
	}

}