package com.terturl.MMO.Util.Listeners.HelperListeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.MMOEquipArmorEvent;
import com.terturl.MMO.Util.JsonFileInterpretter;

import net.minecraft.nbt.NBTTagCompound;

public class ArmorEquipHelperListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void equipArmorFromShiftingOrClicking(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		
		// Make sure the inventory is null
		Inventory i = e.getClickedInventory();
		if(i == null) return;
		
		// Make sure the item has a tag
		ItemStack is = e.getCurrentItem();
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
		if(!stack.hasTag()) return;
		
		// Ensure that it is a custom item
		NBTTagCompound tag = stack.getTag();
		if(!tag.hasKey("CustomItem")) return;
		if(tag.getBoolean("CustomItem") == false) return;
		
		// Make sure it has values and read them
		if(!tag.hasKey("CustomItemValues")) return;
		JSONParser js = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) js.parse(tag.getString("CustomItemValues"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		// Make sure the name and slotType values are present
		if(json == null) return;
		JsonFileInterpretter itemInformation = new JsonFileInterpretter(json);
		if(!itemInformation.contains("name")) return;
		if(!itemInformation.contains("slotType")) return;
		String s = itemInformation.getString("slotType");
		
		// If its not armor we do not care about it
		if(s == "ITEM" || s == "TRINKET" || s == "DROP" || s == "MAIN_HAND" || s == "OFF_HAND") return;
		
		// Make sure this is done where we can see the armor
		if(i.getType().equals(InventoryType.CRAFTING) || i.getType().equals(InventoryType.PLAYER)) {
			// If the item was dropped in the armor slot or was shift clicked into the armor slot
			if(e.getSlotType().equals(InventoryType.SlotType.ARMOR) || e.isShiftClick()) {
				MMOEquipArmorEvent meae = new MMOEquipArmorEvent(MinecraftMMO.getInstance().getItemManager().getItem(itemInformation.getString("name")), (Player)e.getWhoClicked());
				if(meae.isCancelled()) return;
				Bukkit.getPluginManager().callEvent(meae);
			}
		}
	}
	
}