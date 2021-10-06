package com.terturl.MMO.Commands.Debug;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Player.Skills.Crafting.MMORecipe;
import com.terturl.MMO.Player.Skills.Crafting.RecipeInventory;
import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Items.CustomItem.Rarity;
import com.terturl.MMO.Util.Items.CustomItem.SlotType;

public class Crafting extends CraftCommand {

	public Crafting() {
		super("crafting");
	}
	
	public void handleCommand(Player p, String[] args) {
		// Custom Test item being made
		CustomItem ci = new CustomItem("Cobble", Material.COBBLESTONE, 0, 0, Rarity.COMMON);
		ci.setDurability(0.0);
		ci.setMaxDurability(0.0);
		ci.setSlotType(SlotType.DROP);
		
		// Custom items to give to the player
		CustomItem ci2 = new CustomItem("Wood", Material.OAK_PLANKS, 0, 0, Rarity.COMMON);
		ci2.setDurability(0.0);
		ci2.setMaxDurability(0.0);
		ci2.setSlotType(SlotType.DROP);
		
		// Custom Recipe test
		MMORecipe mr = new MMORecipe(ci, 10);
		mr.addItem(ci2, 3);
		MinecraftMMO.getInstance().getRecipeManager().addRecipe(mr);
		
		ItemStack is = ci2.makeItem(10);
		p.getInventory().addItem(is);
		p.updateInventory();
		
		RecipeInventory ri = new RecipeInventory(p);
		ri.open(p);
	}
	
}
