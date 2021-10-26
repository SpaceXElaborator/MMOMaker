package com.terturl.MMO.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Util.JSONHelpers.LocationUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Config class that handles any global variables
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class Configuration {

	private File configurationFile;

	@Getter
	@Setter
	private Location tutorialSpawn;

	@Getter
	@Setter
	private Location mmoClassPicker;

	@Getter
	private List<Location> classSpawnLocations = new ArrayList<>();

	public Configuration() {
		configurationFile = new File(MinecraftMMO.getInstance().getDataFolder(), "config.yml");
		if (!configurationFile.exists()) {
			try {
				// Create new file and set defaults if there are none
				configurationFile.createNewFile();
				Block b = Bukkit.getWorlds().get(0)
						.getHighestBlockAt(new Location(Bukkit.getWorlds().get(0), 9.5, 124, 300.5));
				setTutorialSpawn(b.getLocation().add(0, 1, 0));
				YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);
				config.set("tutorialSpawn", LocationUtils.locationSerializer(tutorialSpawn));

				config.save(configurationFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);
		setTutorialSpawn(LocationUtils.locationDeSerializer(config.getString("tutorialSpawn")));
		setMmoClassPicker(LocationUtils.locationDeSerializer(config.getString("pickMMOClassSpawn")));
		classSpawnLocations.add(LocationUtils.locationDeSerializer(config.getString("PlayerClass1Location")));
		classSpawnLocations.add(LocationUtils.locationDeSerializer(config.getString("PlayerClass2Location")));
		classSpawnLocations.add(LocationUtils.locationDeSerializer(config.getString("PlayerClass3Location")));

		if (config.contains("classNpc")) {
			for (String s : config.getConfigurationSection("classNpc").getKeys(false)) {
				if (MinecraftMMO.getInstance().getClassHandler().containsClass(s)) {
					Location loc = LocationUtils
							.locationDeSerializer(config.getConfigurationSection("classNpc").getString(s));
					NPC npc = new NPC(loc, s);
					MinecraftMMO.getInstance().getNpcHandler().getClassNpcs().add(npc);
				}
			}
		}
	}

}