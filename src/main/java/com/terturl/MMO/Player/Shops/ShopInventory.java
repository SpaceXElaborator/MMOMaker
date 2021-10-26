package com.terturl.MMO.Player.Shops;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.Framework.ClickAction;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;

/**
 * Handles the inventory and what will eventually happen when clicking on a ShopItem in the inventory
 * @author Sean Rahman
 * @since 0.31.0
 *
 */
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
			
			addButton(new InventoryButton(is) {
				@Override
				public void onPlayerClick(Player p, ClickAction action) {
					
				}
			});
		}
		updateInventory();
	}
	
}