package com.terturl.MMO.Util.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.PickUpMMOItemEvent;
import com.terturl.MMO.Inventories.QuestInventory;
import com.terturl.MMO.Util.Items.CustomItem;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Handles all interaction of items from the player
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class ItemInteractionListeners implements Listener {

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (!e.getHand().equals(EquipmentSlot.HAND))
			return;
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
			return;
		if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
			QuestInventory qi = new QuestInventory(
					MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer()));
			qi.open(e.getPlayer());
		}
	}

	@EventHandler
	public void pickUpItem(EntityPickupItemEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		ItemStack item = e.getItem().getItemStack();
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		if (stack.getTag() == null)
			return;
		NBTTagCompound tag = stack.getTag();
		if (tag.getBoolean("CustomItem")) {
			CustomItem ci = MinecraftMMO.getInstance().getItemManager()
					.getItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
			PickUpMMOItemEvent pumie = new PickUpMMOItemEvent(p, ci, item.getAmount());
			if (!pumie.isCancelled())
				Bukkit.getPluginManager().callEvent(pumie);
		}
	}

}