package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class holds information about child and parent relationships to be used
 * for created the item model for representation in the game as well as where
 * the rotation and origin points are for placement later
 * 
 * @author Sean Rahman
 * @since 0.56.0
 *
 */
@ToString
public class BBOOutliner {

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String parent;

	@Getter
	private double[] origin = new double[] { 0.0D, 0.0D, 0.0D };

	@Getter
	private double[] rotation = new double[] { 0.0D, 0.0D, 0.0D };

	@Getter
	@Setter
	private UUID uuid;

	@Getter
	private List<UUID> children = new ArrayList<>();

	/**
	 * Sets the origin of the BBOOutliner to be used for rotating the object
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
	 * Sets the current rotation of the BBOutliner, will mostly be 0,0,0
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setRotation(Double x, Double y, Double z) {
		rotation[0] = x;
		rotation[1] = y;
		rotation[2] = z;
	}

}