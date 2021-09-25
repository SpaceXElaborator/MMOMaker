package com.terturl.MMO.Util.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.Util.Strings.StringUtils;

public class ItemBuilder {

	private ItemStack item;
	
	public ItemBuilder(Material mat) {
		item = new ItemStack(mat);
	}
	
	public ItemBuilder(ItemStack stack) {
		item = stack;
	}
	
	public ItemBuilder amount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder clearName() {
		Material reset = item.getType();
		item = new ItemStack(reset);
		return this;
	}
	
	public ItemBuilder name(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(StringUtils.message(name));
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder lore(String name) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore == null) {
			lore = new ArrayList<>();
		}
		lore.add(StringUtils.message(name));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder lore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder enchantItem(Enchantment ench, int level) {
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
		meta.addEnchant(ench, level, true);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setDamage(int damage) {
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable) {
			Damageable d = (Damageable)meta;
			d.setDamage(damage);
		}
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemStack build() {
		return item;
	}
	
}