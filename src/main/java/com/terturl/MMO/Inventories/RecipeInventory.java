package com.terturl.MMO.Inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.ClickAction;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.Skills.Crafting.MMORecipe;
import com.terturl.MMO.Player.Skills.Crafting.RecipeManager;

import net.md_5.bungee.api.ChatColor;

public class RecipeInventory extends InventoryUI {

	private InventoryButton ibShowTrue = new InventoryButton(new ItemStack(Material.RED_WOOL)) {
		public void onPlayerClick(Player p, ClickAction a) {
			Material mat = getItem().getType();
			if(mat.equals(Material.RED_WOOL)) {
				addAllItems(p);
				setStack(new ItemStack(Material.GREEN_WOOL));
				updateInventory();
			} else {
				showCraftable(p);
				setStack(new ItemStack(Material.RED_WOOL));
				updateInventory();
			}
		}
	};
	
	public RecipeInventory(Player p) {
		super(18, ChatColor.GREEN + "Crafting UI");
		showCraftable(p);
		addButton(ibShowTrue, 17);
		updateInventory();
	}
	
	private void addAllItems(Player p) {
		getAllButtons().clear();
		RecipeManager rm = MinecraftMMO.getInstance().getRecipeManager();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		for(String s : mc.getCraftingRecipes()) {
			MMORecipe mr = rm.getRecipeByName(s);
			if(mr == null) continue;
			addButton(new InventoryButton(mr.getProduct().makeItem()) {
				public void onPlayerClick(Player p, ClickAction ca) {
					mr.craftItem(p);
				}
			});
		}
		addButton(ibShowTrue, 17);
		updateInventory();
	}
	
	private void showCraftable(Player p) {
		getAllButtons().clear();
		RecipeManager rm = MinecraftMMO.getInstance().getRecipeManager();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		for(String s : mc.getCraftingRecipes()) {
			MMORecipe mr = rm.getRecipeByName(s);
			if(mr == null) continue;
			if(mr.PlayerHasAllItems(p)) {
				addButton(new InventoryButton(mr.getProduct().makeItem()) {
					public void onPlayerClick(Player p, ClickAction ca) {
						mr.craftItem(p);
					}
				});
			}
		}
		addButton(ibShowTrue, 17);
		updateInventory();
	}
	
}