package com.terturl.MMO.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Abilities.Ability;
import com.terturl.MMO.Abilities.Ability.AbilityCosts;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Player.Skills.Crafting.CraftingSkill;

import net.md_5.bungee.api.ChatColor;

/**
 * A basic helper class to create ItemStacks to be used for getting specific actions like Abilities
 * @author Sean Rahman
 * @since 0.34.0
 *
 */
public class BasicInventoryItems {

	/**
	 * Returns the ItemStack that will showcase the player's information about their rpg character
	 * @param p The player to get information from
	 * @return Built ItemStack to display
	 */
	public static ItemStack getPlayerClassItem(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "Character Sheet");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "-----Info-----");
		lore.add("Level: " + String.valueOf(mc.getLevel()));
		lore.add("XP: " + String.valueOf(mc.getXp()) + "/" + mc.getNextLevelXP());
		lore.add("Currency: " + String.valueOf(mc.getMoney()));
		lore.add("");
		lore.add(ChatColor.GOLD + "-----Skills-----");
		CraftingSkill sk = mc.getCraftSkill();
		lore.add("Crafting: (" + String.valueOf(sk.getLevel()) + ") " + String.valueOf(sk.getXp()) + "/" + String.valueOf(sk.getXPToLevelUp()));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	/**
	 * Builds an empty and usable Ability ItemStack that can be used to select an ability to perform
	 * @return Built ItemStack
	 */
	public static ItemStack getRegularAbilities() {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GOLD + "Ability Slot");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "Left Click: Set Slot Ability");
		lore.add(ChatColor.GOLD + "Right Click: Use Ability");
		lore.add("Regular ability slot requires you to have to scroll");
		lore.add("to the item and right click to cast");
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	/**
	 * Returns the book that will allow you to scroll/press to it, use the ability, and switch back to main hand quickly
	 * @return Built ItemStack
	 */
	public static ItemStack getQuickActionAbilities() {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GOLD + "Quick Action Slot");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "Left Click: Set Quick Action Ability");
		lore.add(ChatColor.GOLD + "Right Click: Use Ability");
		lore.add("When in battle, scroll to this item");
		lore.add("to automatically cast the ability if the");
		lore.add("cooldown is ready and the mana is available");
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	/**
	 * Returns the book that will display the ability set on the quick action slot
	 * @param a Ability that the player sets it to
	 * @return Built ItemStack with information about that ability
	 */
	public static ItemStack getQuickActionAbility(Ability a) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GOLD + "Quick Action Slot");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Ability: " + a.getName());
		for(AbilityCosts ac : a.getCosts().keySet()) {
			lore.add(ChatColor.BLUE + "Cost: " + ac.toString() + " - " + String.valueOf(a.getCosts().get(ac)));
		}
		lore.add("");
		lore.add(ChatColor.GOLD + "Left Click: Set Quick Action Ability");
		lore.add(ChatColor.GOLD + "Right Click: Use Ability");
		lore.add("When in battle, scroll to this item to automatically cast the ability if the \ncooldown is ready and the mana is available");
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
}