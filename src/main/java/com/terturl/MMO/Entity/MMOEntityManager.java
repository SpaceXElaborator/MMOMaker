package com.terturl.MMO.Entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.SoundInformation;
import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Math.MinMax;

import lombok.Getter;
import net.minecraft.server.level.WorldServer;

/**
 * Manager for creating MMOEntities (Not to be confused with MMOMob) from JSON files
 * @see com.terturl.MMO.Entity.MMOEntity
 * @author Sean Rahman
 * @Since 0.33.0
 *
 */
public class MMOEntityManager {

	private Map<String, MMOEntity> MMOEntities = new HashMap<>();
	
	@Getter
	private List<MMOEntity> aliveEntities = new ArrayList<>();
	
	@Getter
	private File warnings;
	
	public MMOEntityManager() throws IOException {
		File entityFolder = new File(MinecraftMMO.getInstance().getDataFolder(), "entities");
		warnings = new File(entityFolder, "Warnings.txt");
		if(getWarnings().exists()) { getWarnings().delete(); }
		
		// TODO: This needs to be cleaned up a lot
		
		if(entityFolder.listFiles().length != 0) {
			for(File f : entityFolder.listFiles()) {
				if(f.getName().endsWith(".json")) {
					if(checkEntitiy(f)) {
						JsonFileInterpretter config = new JsonFileInterpretter(f);
						String type = config.getString("Type");
						String name = config.getString("Name");
						EntityConversion ec = EntityConversion.valueOf(type.toUpperCase());
						if(ec == null) continue;
						MMOEntity em = new MMOEntity(ec.getType(), ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle(), name);
						
						if(config.contains("XP")) {
							JSONObject jo = config.getObject("XP");
							Double min = jo.containsKey("Min") ? Double.parseDouble(jo.get("Min").toString()) : 0.0;
							Double max = Double.parseDouble(jo.get("Max").toString());
							MinMax mm = new MinMax(min, max);
							em.setGivableXP(mm);
						}
						
						if(config.contains("Currency")) {
							JSONObject jo = config.getObject("Currency");
							Double min = jo.containsKey("Min") ? Double.parseDouble(jo.get("Min").toString()) : 0.0;
							Double max = Double.parseDouble(jo.get("Max").toString());
							MinMax mm = new MinMax(min, max);
							em.setGivableCurrency(mm);
						}
						
						if(config.contains("HurtSound")) {
							JSONObject jo = config.getObject("HurtSound");
							String sound = jo.containsKey("Sound") ? jo.get("Sound").toString() : "minecraft:entity.zombie.hit";
							Float vol = jo.containsKey("Volume") ? Float.valueOf(jo.get("Volume").toString()) : 1.0f;
							Float pitch = jo.containsKey("Pitch") ? Float.valueOf(jo.get("Pitch").toString()) : 1.0f;
							SoundInformation se = new SoundInformation();
							se.setSound(sound);
							se.setVolume(vol);
							se.setPitch(pitch);
							em.setMMOSoundHurt(se);
						}
						
						if(config.contains("DeathSound")) {
							JSONObject jo = config.getObject("DeathSound");
							String sound = jo.containsKey("Sound") ? jo.get("Sound").toString() : "minecraft:entity.zombie.hit";
							Float vol = jo.containsKey("Volume") ? Float.valueOf(jo.get("Volume").toString()) : 1.0f;
							Float pitch = jo.containsKey("Pitch") ? Float.valueOf(jo.get("Pitch").toString()) : 1.0f;
							SoundInformation se = new SoundInformation();
							se.setSound(sound);
							se.setVolume(vol);
							se.setPitch(pitch);
							em.setMMOSoundDeath(se);
						}
						
						if(config.contains("Items")) {
							JSONArray ja = config.getArray("Items");
							for(Object o : ja) {
								JSONObject jo = (JSONObject)o;
								String item = jo.get("Item").toString();
								Integer amount = jo.containsKey("Amount") ? Integer.valueOf(jo.get("Amount").toString()) : 1;
								Double chance = jo.containsKey("Chance") ? Double.parseDouble(jo.get("Chance").toString()) : 50.0;
								CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(item);
								MMOEntityDrop med = new MMOEntityDrop(ci, amount, chance);
								em.getEntityDrops().add(med);
							}
						}
						
						MMOEntities.put(name, em);
					}
				}
			}
		}
	}

	private boolean checkEntitiy(File f) throws IOException {
		boolean load = true;
		
		JsonFileInterpretter config = new JsonFileInterpretter(f);
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		if(!config.contains("Type")) {
			wbw.write("[" + f.getName() + "] 'Type' property is not set!");
			wbw.newLine();
			load = false;
		}
		
		if(!config.contains("Name")) {
			wbw.write("[" + f.getName() + "] 'Name' property is not set!");
			wbw.newLine();
			load = false;
		}
		
		wbw.flush();
		wbw.close();
		return load;
	}
	
	/**
	 * Check for if an entity is an MMOEntity
	 * @param uuid The UUID of the MMOEntity being searched for
	 * @return If the entity is an instance of MMOEntity and if it is alive or not
	 */
	public boolean containsUUID(UUID uuid) {
		for(MMOEntity mo : getAliveEntities()) {
			if(mo.getUniqueID().equals(uuid)) return true;
		}
		return false;
	}
	
	/**
	 * Try to obtain the MMOEntity from a string.
	 * This will return based on the MMOEntities name from the JSON file used to create it
	 * @param name Name of MMOEntity
	 * @return the instance of the MMOEntity, should be cloned
	 */
	public MMOEntity getEntity(String name) {
		if(!MMOEntities.containsKey(name)) return null;
		return MMOEntities.get(name);
	}
	
	/**
	 * Get an MMOEntity based on their UUID.
	 * Should be used in tandem with containsUUID
	 * @param uuid UUID of the alive Entity
	 * @return Either an MMOEntity or a null value if the MMOEntity doesn't exist by UUID
	 */
	public MMOEntity getEntity(UUID uuid) {
		return getAliveEntities().stream().filter(e -> e.getUniqueID().equals(uuid)).findFirst().orElse(null);
	}
	
	/**
	 * Spawns and teleports an MMOEntity to the sepcific location based off the MMOEntity.
	 * MMOEntity is cloned before spawning
	 * @param loc Location to place the MMOEntity
	 * @param me The MMOEntity to spawn to the server
	 */
	public void spawnEntity(Location loc, MMOEntity me) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		MMOEntity mmoE = me.clone();
		world.addEntity(mmoE);
		mmoE.safeTeleport(loc.getX(), loc.getY(), loc.getZ(), false, TeleportCause.COMMAND);
		aliveEntities.add(mmoE);
	}
	
}