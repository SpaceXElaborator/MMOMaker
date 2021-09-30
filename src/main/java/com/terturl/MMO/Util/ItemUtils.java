package com.terturl.MMO.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.md_5.bungee.api.ChatColor;

public class ItemUtils {

	@SuppressWarnings("unchecked")
	public static JSONObject itemToJSON(ItemStack item) {
		JSONObject jo = new JSONObject();
		jo.put("Type", item.getType().toString());
		jo.put("Amount", String.valueOf(item.getAmount()));
		
		if(item.hasItemMeta()) {
			jo.put("Meta", true);
			ItemMeta meta = item.getItemMeta();
			jo.put("Name", meta.getDisplayName());
			if(meta.hasCustomModelData()) jo.put("CIM", String.valueOf(meta.getCustomModelData()));
			if(meta.hasLore()) {
				JSONArray lore = new JSONArray();
				for(String s : meta.getLore()) {
					lore.add(s);
				}
				jo.put("Lore", lore);
			}
		}
		return jo;
	}
	
	public static ItemStack JSONToItem(JSONObject jo) {
		Material mat = Material.valueOf(jo.get("Type").toString());
		ItemStack item = new ItemStack(mat);
		item.setAmount(Integer.valueOf(jo.get("Amount").toString()));
		
		if(jo.containsKey("Meta")) {
			ItemMeta meta = item.getItemMeta();
			if(jo.containsKey("CIM")) meta.setCustomModelData(Integer.valueOf(jo.get("CIM").toString()));
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', jo.get("Name").toString()));
			if(jo.containsKey("Lore")) {
				JSONArray ja = (JSONArray) jo.get("Lore");
				List<String> lore = new ArrayList<>();
				for(Object o : ja) {
					lore.add(ChatColor.translateAlternateColorCodes('&', o.toString()));
				}
				meta.setLore(lore);
			}
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
}