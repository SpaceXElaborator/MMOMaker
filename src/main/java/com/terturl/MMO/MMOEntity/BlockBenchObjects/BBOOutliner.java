package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BBOOutliner {

	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String parent;
	
	@Getter
	private double[] origin = new double[] {0.0D, 0.0D, 0.0D};
	
	@Getter
	private double[] rotation = new double[] {0.0D, 0.0D, 0.0D};
	
	@Getter @Setter
	private UUID uuid;
	
	@Getter
	private List<UUID> children = new ArrayList<>();
	
	public void setOrigin(Double x, Double y, Double z) {
		origin[0] = x;
		origin[1] = y;
		origin[2] = z;
	}
	
	public void setRotation(Double x, Double y, Double z) {
		rotation[0] = x;
		rotation[1] = y;
		rotation[2] = z;
	}
	
}