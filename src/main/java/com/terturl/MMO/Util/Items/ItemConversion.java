package com.terturl.MMO.Util.Items;

import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

import net.minecraft.nbt.NBTTagCompound;

public class ItemConversion {
	
	public static CustomItem SpigotToMMOItem(ItemStack is) {
		if(!isCustomItem(is)) return null;
		JsonObject itemInformation = getCustomItemJSON(is);
		if(itemInformation == null) return null;
		if(!itemInformation.has("name")) return null;
		return MinecraftMMO.getInstance().getItemManager().getItem(itemInformation.get("name").getAsString());
	}
	
	public static MMOEquipable SpigotToMMOEquipable(ItemStack is) {
		if(!isCustomItem(is)) return null;
		JsonObject itemInformation = getCustomItemJSON(is);
		if(itemInformation == null) return null;
		if(!itemInformation.has("name")) return null;
		if(!itemInformation.has("slotType")) return null;
		
		SlotType st = SlotType.valueOf(itemInformation.get("slotType").getAsString().toUpperCase());
		if(st == SlotType.DROP || st == SlotType.ITEM || st == SlotType.TRINKET) return null;
		
		MMOEquipable equip = (MMOEquipable) MinecraftMMO.getInstance().getItemManager().getItem(itemInformation.get("name").getAsString());
		
		return equip;
	}
	
	public static boolean isCustomItem(ItemStack is) {
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
		if(!stack.hasTag()) return false;
		
		// Ensure that it is a custom item
		NBTTagCompound tag = stack.getTag();
		if(!tag.hasKey("CustomItem")) return false;
		if(tag.getBoolean("CustomItem") == false) return false;
		return true;
	}
	
	public static JsonObject getCustomItemJSON(ItemStack is) {
		if(!isCustomItem(is)) return null;
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = stack.getTag();
		// Make sure it has values and read them
		if(!tag.hasKey("CustomItemValues")) return null;
		JsonObject itemInformation = new Gson().fromJson(tag.getString("CustomItemValues"), JsonObject.class);
		
		// Make sure the name and slotType values are present
		if(itemInformation == null) return null;
		return itemInformation;
	}
	
}