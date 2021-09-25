package com.terturl.MMO.Framework;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ControlledItem {

	private Material _mat;
	private String _name;
	
	public ControlledItem(Material mat) {
		_mat = mat;
	}
	
	public void setName(String s) {
		_name = s;
	}
	
	public ItemStack getItem() {
		ItemStack item = new ItemStack(_mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(_name != null ? _name : item.getType().toString());
		item.setItemMeta(meta);
		return item;
	}
	
	public abstract void onLeftClick(Player p);
	public abstract void onRightClick(Player p);
	
}