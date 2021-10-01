package com.terturl.MMO.Player.Shops;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;

public class ShopInventory extends InventoryUI {

	public ShopInventory(Player p, List<ShopItem> itemsForSale) {
		super(27, p.getName() + "'s Shop");
		
		for(ShopItem si : itemsForSale) {
			ItemStack is = si.getItemForSale().makeItem();
			ItemMeta im = is.getItemMeta();
			List<String> lore = im.getLore();
			lore.add(ChatColor.GOLD + "\nPrice: " + String.valueOf(si.getPrice()));
			im.setLore(lore);
			is.setItemMeta(im);
			
			addButton(new InventoryButton(is) {});
		}
		updateInventory();
	}
	
}