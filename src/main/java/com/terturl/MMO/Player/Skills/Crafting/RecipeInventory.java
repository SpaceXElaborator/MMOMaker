package com.terturl.MMO.Player.Skills.Crafting;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;

import net.md_5.bungee.api.ChatColor;

public class RecipeInventory extends InventoryUI {

	public RecipeInventory() {
		super(18, ChatColor.GREEN + "Crafting UI");
		RecipeManager rm = MinecraftMMO.getInstance().getRecipeManager();
		for(MMORecipe mr : rm.getRecipes()) {
			addButton(new InventoryButton(mr.getProduct().makeItem()) {
				
			});
		}
	}
	
}