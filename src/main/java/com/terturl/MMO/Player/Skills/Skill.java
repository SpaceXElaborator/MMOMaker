package com.terturl.MMO.Player.Skills;

import lombok.Getter;
import lombok.Setter;

public abstract class Skill {
	
	@Getter
	private String skillName;
	
	@Getter @Setter
	private Integer level = 1;
	
	@Getter @Setter
	private Double xp = 0.0;
	
	public Skill(String name) {
		skillName = name;
	}
	
	public abstract void addXP(Double d);
	
}