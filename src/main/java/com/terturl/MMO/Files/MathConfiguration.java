package com.terturl.MMO.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import com.terturl.MMO.MinecraftMMO;

import lombok.Getter;

public class MathConfiguration {

	@Getter
	private String skillLevelCalculation;
	
	@Getter
	private String playerLevelEquation;
	
	public MathConfiguration() {
		File configurationFile = new File(MinecraftMMO.getInstance().getDataFolder(), "math.yml");
		if(!configurationFile.exists()) {
			try {
				configurationFile.createNewFile();
				YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);
				config.set("skillLevelEquation", "ceil((NL * 10) + (1.7^NL))");
				config.set("playerLevelEquation", "CL*100*1.5");
				config.save(configurationFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);
		skillLevelCalculation = config.getString("skillLevelEquation");
		playerLevelEquation = config.getString("playerLevelEquation");
	}
	
}