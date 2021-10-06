package com.terturl.MMO.Player.Skills.Crafting;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;

import net.md_5.bungee.api.ChatColor;

public class RecipeInventory extends InventoryUI {

	public RecipeInventory(Player p) {
		super(18, ChatColor.GREEN + "Crafting UI");
		RecipeManager rm = MinecraftMMO.getInstance().getRecipeManager();
		for(MMORecipe mr : rm.getRecipes()) {
			if(mr.PlayerHasAllItems(p)) {
				addButton(new InventoryButton(mr.getProduct().makeItem()) {
					
				});
			}
		}
		updateInventory();
	}
	
}