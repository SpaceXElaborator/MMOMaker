package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import lombok.Getter;
import lombok.Setter;

public class Rotation {

	@Getter @Setter
	private Double angle;
	
	@Getter @Setter
	private String axis;
	
	@Getter
	private Double[] origin;
	
	public void setOrigin(Double x, Double y, Double z) {
		origin = new Double[] {x, y, z};
	}
	
	public void addOrigin(Double... offset) {
		origin[0] = origin[0] + offset[0];
		origin[1] = origin[1] + offset[1];
		origin[2] = origin[2] + offset[2];
	}
	
	public void shrinkOrigin(Double ratio) {
		origin[0] = shrink(origin[0], ratio);
		origin[1] = shrink(origin[1], ratio);
		origin[1] = shrink(origin[2], ratio);
	}
	
	private Double shrink(Double d, Double r) {
		return 8.0D * (1.0D - r) + r * d;
	}
	
}