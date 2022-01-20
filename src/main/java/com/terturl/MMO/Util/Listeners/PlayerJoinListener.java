package com.terturl.MMO.Util.Listeners;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Util.Items.CustomItemManager;
import com.terturl.MMO.Util.Items.MMOEquipable;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

/**
 * Handles the calling of events that will take place when the player joins the
 * server
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PacketListener.injectPlayer(e.getPlayer());
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();

		// Spawn any and all NPCs that have been saved to the list
		for (NPC npc : MinecraftMMO.getInstance().getNpcHandler().getNpcs()) {
			if (MinecraftMMO.getInstance().getNpcHandler().getNpcSkin().containsKey(npc.getDisplayName())) {
				NPC.SkinTextures
						.getByUUID(UUID.fromString(
								MinecraftMMO.getInstance().getNpcHandler().getNpcSkin().get(npc.getDisplayName())))
						.thenAccept(skinTexture -> {
							npc.setSkin(skinTexture);
						});
			}
			npc.spawnNPC(e.getPlayer());
			npc.reloadNPC(e.getPlayer());
		}
		
		// Get all Class NPC's and spawn the NPC to the player and set its equipment
		for (NPC npc : MinecraftMMO.getInstance().getNpcHandler().getClassNpcs()) {
			npc.spawnNPC(e.getPlayer());
			MMOClass mc = MinecraftMMO.getInstance().getClassHandler().getClass(npc.getDisplayName());
			
			ItemStack boots = (cim.getCustomItems().get(mc.getStartItems().get(SlotType.BOOTS)) instanceof MMOEquipable) ? ((MMOEquipable) cim.getCustomItems().get(mc.getStartItems().get(SlotType.BOOTS))).makeItem() : null;
			ItemStack chest = (cim.getCustomItems().get(mc.getStartItems().get(SlotType.CHEST)) instanceof MMOEquipable) ? ((MMOEquipable) cim.getCustomItems().get(mc.getStartItems().get(SlotType.CHEST))).makeItem() : null;
			ItemStack legs = (cim.getCustomItems().get(mc.getStartItems().get(SlotType.LEGS)) instanceof MMOEquipable) ? ((MMOEquipable) cim.getCustomItems().get(mc.getStartItems().get(SlotType.LEGS))).makeItem() : null;
			ItemStack helmet = (cim.getCustomItems().get(mc.getStartItems().get(SlotType.HELMET)) instanceof MMOEquipable) ? ((MMOEquipable) cim.getCustomItems().get(mc.getStartItems().get(SlotType.HELMET))).makeItem() : null;
			ItemStack off_hand = (cim.getCustomItems().get(mc.getStartItems().get(SlotType.OFF_HAND)) instanceof MMOEquipable) ? ((MMOEquipable) cim.getCustomItems().get(mc.getStartItems().get(SlotType.OFF_HAND))).makeItem() : null;
			ItemStack main_hand = (cim.getCustomItems().get(mc.getStartItems().get(SlotType.MAIN_HAND)) instanceof MMOEquipable) ? ((MMOEquipable) cim.getCustomItems().get(mc.getStartItems().get(SlotType.MAIN_HAND))).makeItem() : null;
			
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.HELMET, helmet);
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.CHESTPLATE, chest);
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.LEGGINGS, legs);
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.BOOTS, boots);
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.MAIN_HAND, main_hand);
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.OFF_HAND, off_hand);
		}

		if (!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(e.getPlayer())) {
			e.getPlayer().teleport(MinecraftMMO.getInstance().getMmoConfiguration().getTutorialSpawn());
			e.getPlayer().sendMessage("Welcome to the world of {name to be determined because yeah}");
		} else {
			MinecraftMMO.getInstance().getPlayerHandler().loadPlayer(e.getPlayer());
			e.getPlayer().teleport(MinecraftMMO.getInstance().getMmoConfiguration().getMmoClassPicker());
			AttributeInstance health = e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
			health.setBaseValue(20);
			e.getPlayer().setHealth(20);
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer());
		if (mp.getCurrentCharacter() != -1) {
			MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
			mc.setClassLocation(e.getPlayer().getLocation());
		}
		PacketListener.removePlayer(e.getPlayer());
		MinecraftMMO.getInstance().getPlayerHandler().savePlayerInfo(e.getPlayer());
	}

}