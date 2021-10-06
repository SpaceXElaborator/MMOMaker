package com.terturl.MMO.Player.Skills.Crafting;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class MMORecipe {
	
	@Getter
	private CustomItem product;
	
	@Getter
	private Integer levelRequired;
	
	@Getter @Setter
	private Integer amountToGive;
	
	@Getter @Setter
	private Double xpGiven = 0.0;
	
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
			ItemStack is = ci.makeItem();
			if(!p.getInventory().containsAtLeast(is, recipeMapping.get(ci))) {
				return false;
			}
		}
		return true;
	}
	
}