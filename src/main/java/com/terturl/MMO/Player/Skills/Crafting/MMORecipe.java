package com.terturl.MMO.Player.Skills.Crafting;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.Getter;

public class MMORecipe {
	
	@Getter
	private CustomItem product;
	
	@Getter
	private Integer levelRequired;
	
	@Getter
	private Map<CustomItem, Integer> recipeMapping = new HashMap<>();
	
	public MMORecipe(CustomItem ci, Integer level) {
		product = ci;
		levelRequired = level;
	}
	
	public void addItem(CustomItem ci, Integer amount) {
		recipeMapping.put(ci, amount);
	}
	
	public boolean PlayerHasAllItems(Player p) {
		for(CustomItem ci : recipeMapping.keySet()) {
			ItemStack is = ci.makeItem(recipeMapping.get(ci));
			if(!p.getInventory().contains(is)) {
				return false;
			}
		}
		return true;
	}
	
}