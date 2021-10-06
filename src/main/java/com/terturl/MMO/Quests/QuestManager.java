package com.terturl.MMO.Quests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Quests.Subquests.BasicQuest;
import com.terturl.MMO.Quests.Subquests.EntityKillQuest;
import com.terturl.MMO.Quests.Subquests.LocationQuest;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.LocationUtils;

import lombok.Getter;

public class QuestManager {

	@Getter
	private List<Quest> allQuests = new ArrayList<>();
	
	public QuestManager() {
		File questDir = new File(MinecraftMMO.getInstance().getDataFolder(), "quests");
		if(!questDir.exists()) questDir.mkdir();
		
		for(File f : questDir.listFiles()) {
			if(f.getName().endsWith(".json")) {
				Object q = null;
				JsonFileInterpretter config = new JsonFileInterpretter(f);
				String name = config.contains("Name") ? config.getString("Name") : null;
				String descString = config.contains("Description") ? config.getString("Description") : null;
				String presentString = config.contains("Present") ? config.getString("Present") : "Would you like to ACCEPT or DENY?";
				String denyString = config.contains("Deny") ? config.getString("Deny") : "That's Very Sad Traveller";
				String acceptString = config.contains("Accept") ? config.getString("Accept") : "Thank you Traveller!";
				String type = config.contains("Type") ? config.getString("Type") : "Basic";
				Double money = config.contains("Money") ? config.getDouble("Money") : 0.0;
				Double xp = config.contains("XP") ? config.getDouble("XP") : 0.0;
				List<String> customItems = config.contains("Items") ? config.getStringList("Items") : new ArrayList<>();
				List<String> questRewards = config.contains("RewardQuests") ? config.getStringList("RewardQuests") : new ArrayList<>();
				List<String> parentQuests = config.contains("Parents") ? config.getStringList("Parents") : new ArrayList<>();
				List<String> craftingRecipes = config.contains("CraftingRecipes") ? config.getStringList("CraftingRecipes") : new ArrayList<>();
				if(name == null || descString == null) continue;

				if(type.equalsIgnoreCase("location")) {
					q = new LocationQuest(name, LocationUtils.locationDeSerializer(config.getString("Location")));
				} else if(type.equalsIgnoreCase("killEntity")) { 
					q = new EntityKillQuest(name);
					JSONArray ja = config.getArray("EntityInformation");
					for(Object o : ja) {
						JSONObject jo = (JSONObject)o;
						EntityType et = EntityType.valueOf(jo.get("Type").toString().toUpperCase());
						Integer amount = Integer.parseInt(jo.get("Amount").toString());
						((EntityKillQuest) q).addEntityToKill(et, amount);
					}
				} else {
					q = new BasicQuest(name);
				}
				
				((Quest) q).setDescString(descString);
				((Quest) q).setAcceptString(acceptString);
				((Quest) q).setDenyString(denyString);
				((Quest) q).setPresentString(presentString);
				((Quest) q).setParentQuests(parentQuests);
				((Quest) q).setXp(xp);
				((Quest) q).setMoney(money);
				((Quest) q).setItems(customItems);
				((Quest) q).setChildQuests(questRewards);
				((Quest) q).setRecipes(craftingRecipes);
				allQuests.add((Quest) q);
			}
		}
	}
	
	public Quest getQuest(String s) {
		return allQuests.stream().filter(e -> e.getName().equals(s)).findFirst().orElse(null);
	}
	
}