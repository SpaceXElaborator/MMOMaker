package com.terturl.MMO.Util.Items;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class SkullCreator {

	public static ItemStack getSkull(String value) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		if(value.isEmpty()) return head;
		
		SkullMeta meta = (SkullMeta)head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		
		profile.getProperties().put("textures", new Property("textures", value));
		try {
			Method mtd = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
			mtd.setAccessible(true);
			mtd.invoke(meta, profile);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		head.setItemMeta(meta);
		return head;
	}
	
}