package com.terturl.MMO.Player.Skills.Crafting;

import com.terturl.MMO.Player.Skills.Skill;

public class CraftingSkill extends Skill {
	
	public CraftingSkill() {
		super("Crafting");
	}
	
	public CraftingSkill clone() {
		CraftingSkill sk = new CraftingSkill();
		sk.setLevel(getLevel());
		sk.setXp(getXp());
		return sk;
	}
	
}