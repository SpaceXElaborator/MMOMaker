package com.terturl.MMO.Util.Items;

import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

import net.minecraft.nbt.NBTTagCompound;

public class ItemConversion {
	
	public static CustomItem SpigotToMMOItem(ItemStack is) {
		if(!isCustomItem(is)) return null;
		JSONObject json = getCustomItemJSON(is);
		if(json == null) return null;
		JsonFileInterpretter itemInformation = new JsonFileInterpretter(json);
		if(!itemInformation.contains("name")) return null;
		return MinecraftMMO.getInstance().getItemManager().getItem(itemInformation.getString("name"));
	}
	
	public static MMOEquipable SpigotToMMOEquipable(ItemStack is) {
		if(!isCustomItem(is)) return null;
		JSONObject json = getCustomItemJSON(is);
		if(json == null) return null;
		JsonFileInterpretter itemInformation = new JsonFileInterpretter(json);
		if(!itemInformation.contains("name")) return null;
		if(!itemInformation.contains("slotType")) return null;
		
		SlotType st = SlotType.valueOf(itemInformation.getString("slotType"));
		if(st == SlotType.DROP || st == SlotType.ITEM || st == SlotType.TRINKET) return null;
		
		MMOEquipable equip = (MMOEquipable) MinecraftMMO.getInstance().getItemManager().getItem(itemInformation.getString("name"));
		
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
	
	public static JSONObject getCustomItemJSON(ItemStack is) {
		if(!isCustomItem(is)) return null;
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = stack.getTag();
		// Make sure it has values and read them
		if(!tag.hasKey("CustomItemValues")) return null;
		JSONParser js = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) js.parse(tag.getString("CustomItemValues"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		// Make sure the name and slotType values are present
		if(json == null) return null;
		return json;
	}
	
}