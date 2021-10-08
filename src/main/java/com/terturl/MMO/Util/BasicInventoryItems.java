package com.terturl.MMO.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.Skills.Crafting.CraftingSkill;

import net.md_5.bungee.api.ChatColor;

public class BasicInventoryItems {

	public static ItemStack getPlayerClassItem(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "Character Sheet");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "-----Info-----");
		lore.add("Level: " + String.valueOf(mc.getLevel()));
		lore.add("XP: " + String.valueOf(mc.getXp()) + "/50");
		lore.add("Currency: " + String.valueOf(mc.getMoney()));
		lore.add("");
		lore.add(ChatColor.GOLD + "-----Skills-----");
		CraftingSkill sk = mc.getCraftSkill();
		lore.add("Crafting: (" + String.valueOf(sk.getLevel()) + ") " + String.valueOf(sk.getXp()) + "/" + String.valueOf(sk.getXPToLevelUp()));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
}