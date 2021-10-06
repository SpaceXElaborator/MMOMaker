package com.terturl.MMO.Player.Skills.Crafting;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class RecipeManager {

	@Getter
	private List<MMORecipe> recipes = new ArrayList<>();
	
	public RecipeManager() {}
	
	public void addRecipe(MMORecipe mr) {
		if(recipes.contains(mr)) return;
		recipes.add(mr);
	}
	
}