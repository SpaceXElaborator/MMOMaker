package com.terturl.MMO.Util.Items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;
import com.terturl.MMO.Util.Items.ItemEnums.Rarity;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

import lombok.Getter;
import lombok.Setter;

public class CustomArmor extends MMOEquipable {

	@Getter
	@Setter
	private Color color;

	public CustomArmor(String name, Material mat, Integer itemDamage, Integer level, Rarity rarity, CraftRarity craft, SlotType st) {
		super(name, mat, itemDamage, rarity, craft, level);
		setSlotType(st);
	}

	public CustomArmor(String name, Material mat, Integer itemDamage, Integer level, Rarity rarity, CraftRarity craft,
			Color c, SlotType st) {
		super(name, mat, itemDamage, rarity, craft, level);
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