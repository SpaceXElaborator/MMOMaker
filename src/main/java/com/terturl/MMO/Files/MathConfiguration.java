package com.terturl.MMO.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import com.terturl.MMO.MinecraftMMO;

import lombok.Getter;

public class MathConfiguration {

	@Getter
	private String skillLevelCalculation;
	
	public MathConfiguration() {
		File configurationFile = new File(MinecraftMMO.getInstance().getDataFolder(), "math.yml");
		if(!configurationFile.exists()) {
			try {
				configurationFile.createNewFile();
				YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);
				config.set("skillLevelEquation", "(NL * 10) + (1.7^NL)");
				
				config.save(configurationFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);
		skillLevelCalculation = config.getString("skillLevelEquation");
	}
	
}