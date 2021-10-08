package com.terturl.MMO.Quests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;

import lombok.Getter;

public class QuestManager {

	@Getter
	private List<Quest> allQuests = new ArrayList<>();
	
	private Map<String, Quest> questTypes = new HashMap<>();
	
	private File questDir;
	
	public QuestManager() {
		questDir = new File(MinecraftMMO.getInstance().getDataFolder(), "quests");
		if(!questDir.exists()) questDir.mkdir();
	}
	
	public void loadQuests() {
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
				
				// TODO: Combine these into one Rewards JSONObject
				List<String> customItems = config.contains("Items") ? config.getStringList("Items") : new ArrayList<>();
				List<String> questRewards = config.contains("RewardQuests") ? config.getStringList("RewardQuests") : new ArrayList<>();
				List<String> parentQuests = config.contains("Parents") ? config.getStringList("Parents") : new ArrayList<>();
				List<String> craftingRecipes = config.contains("CraftingRecipes") ? config.getStringList("CraftingRecipes") : new ArrayList<>();
				
				// TODO: Make this a check method
				if(name == null || descString == null) continue;
				if(!questTypes.containsKey(type)) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, "Quest File: " + f.getName() + " has unknown type");
					continue;
				}
				if(!config.contains("Properties")) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, "Quest File: " + f.getName() + " has no properties field to load quest information");
					continue;
				}
				
				q = (Quest) questTypes.get(type).clone();
				if(q == null) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, "Quest File: " + f.getName() + " unable to create new. Check clone() method");
					continue;
				}
				
				((Quest) q).loadQuest(config.getObject("Properties"));
				((Quest) q).setName(name);
				((Quest) q).setQuestType(type);
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
	
	public void registerQuest(String type, Quest baseClass) {
		questTypes.put(type, baseClass);
	}
	
	public Quest getQuest(String s) {
		return allQuests.stream().filter(e -> e.getName().equals(s)).findFirst().orElse(null);
	}
	
}