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
import com.terturl.MMO.Entity.Util.MMOEntityDrop;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.MinMax;
import com.terturl.MMO.Util.Items.CustomItem;

import lombok.Getter;
import net.minecraft.server.level.WorldServer;

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
						
						if(config.contains("Items")) {
							JSONArray ja = config.getArray("Items");
							for(Object o : ja) {
								JSONObject jo = (JSONObject)o;
								String item = jo.get("Item").toString();
								Integer amount = jo.containsKey("Amount") ? Integer.valueOf(jo.get("Amount").toString()) : 1;
								Integer chance = jo.containsKey("Chance") ? Integer.valueOf(jo.get("Chance").toString()) : 50;
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
	
	public boolean containsUUID(UUID uuid) {
		for(MMOEntity mo : getAliveEntities()) {
			if(mo.getUniqueID().equals(uuid)) return true;
		}
		return false;
	}
	
	public MMOEntity getEntity(String name) {
		if(!MMOEntities.containsKey(name)) return null;
		return MMOEntities.get(name);
	}
	
	public MMOEntity getEntity(UUID uuid) {
		return getAliveEntities().stream().filter(e -> e.getUniqueID().equals(uuid)).findFirst().orElse(null);
	}
	
	public void spawnEntity(Location loc, MMOEntity me) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		MMOEntity mmoE = me.clone();
		world.addEntity(mmoE);
		mmoE.safeTeleport(loc.getX(), loc.getY(), loc.getZ(), false, TeleportCause.COMMAND);
		aliveEntities.add(mmoE);
	}
	
}