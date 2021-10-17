package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import lombok.Getter;

public class Display {

	@Getter
	private Double[] translation = new Double[] {0.0D, -6.4D, 0.0D};
	
	@Getter
	private Double[] rotation = new Double[] {0.0D, 0.0D, 0.0D};
	
	@Getter
	private Double[] scale = new Double[3];
	
	public void moveTranslation(Double... offset) {
		translation[0] = translation[0] - offset[0] * scale[0];
		translation[1] = translation[1] - offset[1] * scale[1];
		translation[2] = translation[2] - offset[2] * scale[2];
	}
	
	public void setTranslation(Double x, Double y, Double z) {
		translation[0] = x;
		translation[1] = y;
		translation[2] = z;
	}
	
	public void setRotation(Double x, Double y, Double z) {
		rotation[0] = x;
		rotation[1] = y;
		rotation[2] = z;
	}
	
	public void setScale(Double x, Double y, Double z) {
		scale[0] = x;
		scale[1] = y;
		scale[2] = z;
	}
	
}