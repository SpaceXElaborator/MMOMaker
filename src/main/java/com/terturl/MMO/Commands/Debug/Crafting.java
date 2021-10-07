package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Inventories.RecipeInventory;

public class Crafting extends CraftCommand {

	public Crafting() {
		super("crafting");
	}
	
	public void handleCommand(Player p, String[] args) {
		RecipeInventory ri = new RecipeInventory(p);
		ri.open(p);
	}
	
}