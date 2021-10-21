package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BBOBone {

	@Getter @Setter
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
	
	public BBOBone(String p, Double x, Double y, Double z, Double rx, Double ry, Double rz) {
		parent = p;
		globalOffset = new Double[] { x/16.0D, y/16.0D, z/16.0D };
		localRotation = new Double[] { rx, ry, rz };
	}
	
	public void setRelativeOffset(Double... offset) {
		localOffset = new Double[] { globalOffset[0] - offset[0], globalOffset[1] - offset[1], globalOffset[2] - offset[2] };
	}
	
	public void addChild(String boneName, BBOBone bone) {
		children.put(boneName, bone);
	}
	
	public void setOption(String option, Boolean val) {
		options.put(option, val);
	}
	
	public void updateChildren() {
		if(children.isEmpty()) return;
		children.values().forEach(e -> {
			e.setRelativeOffset(globalOffset);
			e.updateChildren();
		});
	}
	
}