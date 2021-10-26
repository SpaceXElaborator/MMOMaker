package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A representation of a .bbmodels cube and its placement and rotation in the
 * file
 * 
 * @author Sean Rahman
 * @since 0.55.0
 *
 */
@ToString
public class BBOCube {

	@Getter
	@Setter
	private String name;

	@Getter
	private double[] from = new double[3];

	@Getter
	private double[] to = new double[3];

	@Getter
	private double[] origin = new double[] { 0.0D, 0.0D, 0.0D };

	@Getter
	private double[] rotation = new double[] { 0.0D, 0.0D, 0.0D };

	@Getter
	private Double inflate = 0.0D;

	@Getter
	@Setter
	private UUID uuid;

	@Getter
	private Map<String, BBOFace> faces = new HashMap<>();

	/**
	 * Will set the upperleft most position
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setFrom(Double x, Double y, Double z) {
		from[0] = x;
		from[1] = y;
		from[2] = z;
	}

	/**
	 * Will set the lowerright most position
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTo(Double x, Double y, Double z) {
		to[0] = x;
		to[1] = y;
		to[2] = z;
	}

	/**
	 * Will set where the focal point of the cube is for rotating
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setOrigin(Double x, Double y, Double z) {
		origin[0] = x;
		origin[1] = y;
		origin[2] = z;
	}

	/**
	 * Will set the blocks rotation in relation to the origin
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setRotation(Double x, Double y, Double z) {
		rotation[0] = x;
		rotation[1] = y;
		rotation[2] = z;
	}

	/**
	 * Will return how the cube was rotated
	 * @return
	 */
	public String getAxis() {
		if (rotation.length == 0)
			return "x";
		if (rotation[0] != 0.0D)
			return "x";
		if (rotation[1] != 0.0D)
			return "y";
		if (rotation[2] != 0.0D)
			return "z";
		return "x";
	}

}