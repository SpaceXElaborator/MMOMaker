package com.terturl.MMO.Player.Skills;

import lombok.Getter;
import lombok.Setter;

public abstract class Skill {
	
	@Getter
	private String skillName;
	
	@Getter @Setter
	private Integer level = 0;
	
	@Getter @Setter
	private Double xp = 0.0;
	
	public Skill(String name) {
		skillName = name;
	}
	
	public void addXP(Double d) {
		Double finalXP = ((double)level+1) * 10 + (Math.pow(1.7, (double)level));
		if(xp+d >= finalXP) {
			Double rollOver = (xp+d) - finalXP;
			level = level + 1;
			addXP(rollOver);
		}
		xp += d;
	}
	
}