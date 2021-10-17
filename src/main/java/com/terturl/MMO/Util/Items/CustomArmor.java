package com.terturl.MMO.Util.Items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import lombok.Getter;
import lombok.Setter;

public class CustomArmor extends CustomItem {

	@Getter
	@Setter
	private Color color;

	public CustomArmor(String name, Material mat, Integer itemDamage, Integer level, Rarity rarity, SlotType st) {
		super(name, mat, itemDamage, level, rarity);
		setSlotType(st);
	}

	public CustomArmor(Player p, String name, Material mat, Integer itemDamage, Integer level, Rarity rarity, CraftRarity craft,
			Color c, SlotType st) {
		super(p, name, mat, itemDamage, level, rarity, craft);
		color = c;
		setSlotType(st);
	}
	
	public ItemStack makeItem() {
		ItemStack stack = super.makeItem();
		if (stack.getType().equals(Material.LEATHER_BOOTS) 
				|| stack.getType().equals(Material.LEATHER_LEGGINGS)
				|| stack.getType().equals(Material.LEATHER_CHESTPLATE)
				|| stack.getType().equals(Material.LEATHER_HELMET)) {
			LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
			if (getColor() != null) {
				meta.setColor(getColor());
			}
			stack.setItemMeta(meta);
		}
		return stack;
	}

}