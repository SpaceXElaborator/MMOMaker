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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
						JsonObject config = new JsonFileInterpretter(f).getJson();
						String type = config.get("Type").getAsString();
						String name = config.get("Name").getAsString();
						EntityConversion ec = EntityConversion.valueOf(type.toUpperCase());
						if(ec == null) continue;
						MMOEntity em = new MMOEntity(ec.getType(), ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle(), name);
						
						if(config.has("XP") && config.get("XP").isJsonObject()) {
							JsonObject jo = config.get("XP").getAsJsonObject();
							double min = jo.has("Min") ? jo.get("Min").getAsDouble() : 0.0;
							double max = jo.has("Max") ? jo.get("Max").getAsDouble() : 0.0;
							MinMax mm = new MinMax(min, max);
							em.setGivableXP(mm);
						}
						
						if(config.has("Currency") && config.get("Currency").isJsonObject()) {
							JsonObject jo = config.get("Currency").getAsJsonObject();
							double min = jo.has("Min") ? jo.get("Min").getAsDouble() : 0.0;
							double max = jo.has("Max") ? jo.get("Max").getAsDouble() : 0.0;
							MinMax mm = new MinMax(min, max);
							em.setGivableCurrency(mm);
						}
						
						if(config.has("HurtSound") && config.get("HurtSound").isJsonObject()) {
							JsonObject jo = config.get("HurtSound").getAsJsonObject();
							String sound = jo.has("Sound") ? jo.get("Sound").getAsString() : "minecraft:entity.zombie.hit";
							float vol = jo.has("Volume") ? jo.get("Volume").getAsFloat() : 1.0f;
							float pitch = jo.has("Pitch") ? jo.get("Pitch").getAsFloat() : 1.0f;
							SoundInformation se = new SoundInformation();
							se.setSound(sound);
							se.setVolume(vol);
							se.setPitch(pitch);
							em.setMMOSoundHurt(se);
						}
						
						if(config.has("DeathSound") && config.get("DeathSound").isJsonObject()) {
							JsonObject jo = config.get("DeathSound").getAsJsonObject();
							String sound = jo.has("Sound") ? jo.get("Sound").getAsString() : "minecraft:entity.zombie.hit";
							float vol = jo.has("Volume") ? jo.get("Volume").getAsFloat() : 1.0f;
							float pitch = jo.has("Pitch") ? jo.get("Pitch").getAsFloat() : 1.0f;
							SoundInformation se = new SoundInformation();
							se.setSound(sound);
							se.setVolume(vol);
							se.setPitch(pitch);
							em.setMMOSoundDeath(se);
						}
						
						if(config.has("Items") && config.get("Items").isJsonArray()) {
							JsonArray ja = config.get("Items").getAsJsonArray();
							for(JsonElement je : ja) {
								if(!je.isJsonObject()) continue;
								JsonObject jo = je.getAsJsonObject();
								String item = jo.get("Item").getAsString();
								int amount = jo.has("Amount") ? jo.get("Amount").getAsInt() : 1;
								double chance = jo.has("Chance") ? jo.get("Chance").getAsDouble() : 50.0;
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
		
		JsonObject config = new JsonFileInterpretter(f).getJson();
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		if(!config.has("Type")) {
			wbw.write("[" + f.getName() + "] 'Type' property is not set!");
			wbw.newLine();
			load = false;
		}
		
		if(!config.has("Name")) {
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