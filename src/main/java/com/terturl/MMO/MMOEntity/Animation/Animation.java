package com.terturl.MMO.MMOEntity.Animation;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Animation {

	@Getter @Setter
	private UUID uuid;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private Boolean isOverride;
	
	@Getter @Setter
	private Integer length;
	
	@Getter @Setter
	private String loop;
	
	public Animation() {
		
	}
	
}