package com.terturl.MMO.Entity.NPC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.base.Objects;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.JSONHelpers.LocationUtils;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * A manager used to read NPC json files and spawn then at the correct location
 * with a list of Quest that they can give as well as default idle string that
 * will be sent to the player if they interact with the NPC without any quests
 * givable
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class NPCManager {

	@Getter
	private List<NPC> npcs = new ArrayList<>();
	@Getter
	private List<NPC> classNpcs = new ArrayList<>();

	@Getter
	private Map<String, String> npcSkin = new HashMap<>();

	public NPCManager() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering NPCs...");
		File npcDir = new File(MinecraftMMO.getInstance().getDataFolder(), "npcs");
		if (!npcDir.exists())
			npcDir.mkdir();
		for (File f : npcDir.listFiles()) {
			if (f.getName().endsWith(".json")) {
				JsonFileInterpretter config = new JsonFileInterpretter(f);
				String name = config.getString("Name");
				UUID skin = config.contains("SkinUUID") ? UUID.fromString(config.getString("SkinUUID")) : null;
				Location loc = LocationUtils.locationDeSerializer(config.getString("Location"));
				String idleString = config.contains("IdleString") ? config.getString("IdleString") : "Hello Player";
				List<String> quests = config.contains("Quests") ? config.getStringList("Quests") : new ArrayList<>();
				NPC npc = new NPC(loc, name);
				if (skin != null) {
					npcSkin.put(name, skin.toString());
				}
				npc.setIdleString(idleString);

				List<String> givableQuests = new ArrayList<>();
				quests.forEach(e -> {
					givableQuests.add(e);
				});

				npc.setGivableQuest(givableQuests);
				npcs.add(npc);
			}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done...");
	}

	/**
	 * Will find the NPC that contains a quest
	 * 
	 * @param q Quest to find NPC by
	 * @return NPC or null
	 */
	public NPC findNpcWithQuest(String q) {
		return npcs.stream().filter(e -> e.getGivableQuest().contains(q)).findFirst().orElse(null);
	}

	/**
	 * Checks to see if the EntityID given is that of an NPC
	 * 
	 * @param id Entity ID to check
	 * @return If Entity ID is NPC
	 */
	public boolean isNpc(Integer id) {
		for (NPC npc : getNpcs()) {
			if (Objects.equal(npc.getEntityID(), id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the Entity ID provided is that of a Class NPC
	 * 
	 * @param id Entity ID to check
	 * @return If Entity ID is Class NPC
	 */
	public boolean isClassNpc(Integer id) {
		for (NPC npc : getClassNpcs()) {
			if (Objects.equal(npc.getEntityID(), id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get NPC by Entity ID
	 * 
	 * @param id Entity ID of NPC
	 * @return NPC or Null
	 */
	public NPC getNPC(Integer id) {
		return npcs.stream().filter(e -> Objects.equal(e.getEntityID(), id)).findFirst().orElse(null);
	}

	/**
	 * Get Class NPC by Entity ID
	 * 
	 * @param id Entity ID of NPC
	 * @return NPC or Null
	 */
	public NPC getClassNPC(Integer id) {
		return classNpcs.stream().filter(e -> Objects.equal(e.getEntityID(), id)).findFirst().orElse(null);
	}

	/**
	 * Spawn NPC at location with the given name
	 * 
	 * @param loc Location for NPC
	 * @param s   Name of NPC
	 */
	public void spawnNPC(Location loc, String s) {
		NPC npc = new NPC(loc, s);
		npc.spawnNPC(loc.getWorld().getPlayers());
		npc.setPing(NPC.Ping.FIVE_BARS);
		npc.setGameMode(NPC.Gamemode.CREATIVE);
		npcs.add(npc);
	}

	/**
	 * Removes NPC from the given location world with the given name
	 * 
	 * @param loc Location for world removal
	 * @param s   Name of NPC
	 */
	public void removeNPC(Location loc, String s) {
		NPC npc = npcs.stream().filter(e -> e.getDisplayName().equalsIgnoreCase(s)).findFirst().orElse(null);
		if (npc == null)
			return;
		npc.removeFromTabList(loc.getWorld().getPlayers());
		npc.destroyNPC(loc.getWorld().getPlayers());
	}

}