package com.terturl.MMO.Player.MMOClasses;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.Player.MMOClass;

public class ArcherClass extends MMOClass {

	public ArcherClass() {
		super("Archer");
	}

	// Starter armor
	public ItemStack starterHelmet() {
		return new ItemStack(Material.LEATHER_HELMET);
	}

	public ItemStack starterChestplate() {
		return new ItemStack(Material.LEATHER_CHESTPLATE);
	}

	public ItemStack starterLeggings() {
		return new ItemStack(Material.LEATHER_LEGGINGS);
	}

	public ItemStack starterBoots() {
		return new ItemStack(Material.LEATHER_BOOTS);
	}

	@Override
	public ItemStack startMainHand() {
		return new ItemStack(Material.BOW);
	}

	@Override
	public ItemStack startOffHand() {
		// TODO Auto-generated method stub
		return null;
	}
	
}