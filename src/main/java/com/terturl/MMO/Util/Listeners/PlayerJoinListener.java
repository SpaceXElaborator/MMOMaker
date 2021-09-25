package com.terturl.MMO.Util.Listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Util.NPC;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PacketListener.injectPlayer(e.getPlayer());
		
		// Spawn any and all NPCs that have been saved to the list
		for (NPC npc : MinecraftMMO.getInstance().getNpcHandler().getNpcs()) {
			if(MinecraftMMO.getInstance().getNpcHandler().getNpcSkin().containsKey(npc.getDisplayName())) {
				NPC.SkinTextures.getByUUID(UUID.fromString(MinecraftMMO.getInstance().getNpcHandler().getNpcSkin().get(npc.getDisplayName()))).thenAccept(skinTexture -> {
					npc.setSkin(skinTexture);
				});
			}
			npc.spawnNPC(e.getPlayer());
			npc.reloadNPC(e.getPlayer());
		}
		
		for(NPC npc : MinecraftMMO.getInstance().getNpcHandler().getClassNpcs()) {
			npc.spawnNPC(e.getPlayer());
			MMOClass mmoClass = MinecraftMMO.getInstance().getClassHandler().getClass(npc.getDisplayName());
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.HELMET, mmoClass.starterHelmet());
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.CHESTPLATE, mmoClass.starterChestplate());
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.LEGGINGS, mmoClass.starterLeggings());
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.BOOTS, mmoClass.starterBoots());
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.MAIN_HAND, mmoClass.startMainHand());
			npc.setEquipment(e.getPlayer(), NPC.ItemSlot.OFF_HAND, mmoClass.startOffHand());
		}
		
		if (!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(e.getPlayer())) {
			e.getPlayer().teleport(MinecraftMMO.getInstance().getMmoConfiguration().getTutorialSpawn());
			e.getPlayer().sendMessage("Welcome to the world of {name to be determined because yeah}");
		} else {
			MinecraftMMO.getInstance().getPlayerHandler().loadPlayer(e.getPlayer());
			e.getPlayer().teleport(MinecraftMMO.getInstance().getMmoConfiguration().getMmoClassPicker());
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer());
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		mc.setClassLocation(e.getPlayer().getLocation());
		PacketListener.removePlayer(e.getPlayer());
		MinecraftMMO.getInstance().getPlayerHandler().savePlayerInfo(e.getPlayer());
	}

}