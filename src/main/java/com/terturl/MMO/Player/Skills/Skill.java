package com.terturl.MMO.Player.Skills;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import com.terturl.MMO.MinecraftMMO;

import lombok.Getter;
import lombok.Setter;

public abstract class Skill implements Cloneable {
	
	@Getter
	private String skillName;
	
	@Getter @Setter
	private Integer level = 0;
	
	@Getter @Setter
	private Double xp = 0.0;
	
	public Skill(String name) {
		skillName = name;
	}
	
	public double getXPToLevelUp() {
		Argument CL = new Argument("CL = " + String.valueOf(level));
		Argument NL = new Argument("NL = " + String.valueOf(level + 1));
		Argument XP = new Argument("XP = " + String.valueOf(xp));
		Expression e = new Expression(MinecraftMMO.getInstance().getMathConfig().getSkillLevelCalculation(), CL, NL, XP);
		Double finalXP = e.calculate();
		return finalXP;
	}
	
	public void addXP(Double d) {
		Double finalXP = getXPToLevelUp();
		if(xp+d >= finalXP) {
			Double rollOver = (xp+d) - finalXP;
			level = level + 1;
			addXP(rollOver);
		}
		xp += d;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return level;
	}
	
}