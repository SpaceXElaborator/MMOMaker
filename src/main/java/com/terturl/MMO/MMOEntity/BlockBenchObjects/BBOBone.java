package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A more indepth depiction of the BBOOutliner and holds more information that
 * will be used to update the location of MMOMob parts
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
@ToString
public class BBOBone {

	@Getter
	@Setter
	private String parent = "";

	@Getter
	private Double[] globalOffset = new Double[3];

	@Getter
	private Double[] localOffset = new Double[3];

	@Getter
	private Double[] localRotation = new Double[3];

	@Getter
	private Map<String, Boolean> options = new HashMap<>();

	@Getter
	private Map<String, BBOBone> children = new HashMap<>();

	/**
	 * Create a bone with the following global offset and local rotation
	 * @param p Name of Bone
	 * @param x Bone's X location
	 * @param y Bone's y location
	 * @param z Bone's Z location
	 * @param rx Bone's X rotation
	 * @param ry Bone's y rotation
	 * @param rz Bone's Z rotation
	 */
	public BBOBone(String p, Double x, Double y, Double z, Double rx, Double ry, Double rz) {
		parent = p;
		globalOffset = new Double[] { x / 16.0D, y / 16.0D, z / 16.0D };
		localRotation = new Double[] { rx, ry, rz };
	}

	/**
	 * Set the local offset of the bone in relation to the global offset of the parent/.bbmodel file
	 * @param offset
	 */
	public void setRelativeOffset(Double... offset) {
		localOffset = new Double[] { globalOffset[0] - offset[0], globalOffset[1] - offset[1],
				globalOffset[2] - offset[2] };
	}

	/**
	 * Will add the new BBOBone as a child in the BBOBones children list
	 * @param boneName Name of bone
	 * @param bone new BBOBone to be added as a child
	 */
	public void addChild(String boneName, BBOBone bone) {
		children.put(boneName, bone);
	}

	/**
	 * Is used to set special information about the BBOBone
	 * @param option Option key
	 * @param val Option value
	 */
	public void setOption(String option, Boolean val) {
		options.put(option, val);
	}

	/**
	 * Updates all children's local offset to that of the parents
	 */
	public void updateChildren() {
		if (children.isEmpty())
			return;
		children.values().forEach(e -> {
			e.setRelativeOffset(globalOffset);
			e.updateChildren();
		});
	}

}