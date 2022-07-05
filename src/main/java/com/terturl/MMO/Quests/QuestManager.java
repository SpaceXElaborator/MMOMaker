package com.terturl.MMO.Quests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * Handles the creation and loading of Quests
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class QuestManager {

	@Getter
	private List<Quest> allQuests = new ArrayList<>();

	private Map<String, Quest> questTypes = new HashMap<>();

	@Getter
	private File warnings;

	private File questDir;

	/**
	 * Creates the quests folder at /plugins/MinecraftMMO/quests
	 */
	public QuestManager() {
		questDir = new File(MinecraftMMO.getInstance().getDataFolder(), "quests");
		if (!questDir.exists())
			questDir.mkdir();
		warnings = new File(questDir, "Warnings.txt");
		if (getWarnings().exists()) {
			getWarnings().delete();
		}

		try {
			getWarnings().createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load quests from the quests folder
	 */
	public void loadQuests() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Quests...");
		for (File f : questDir.listFiles()) {
			if (f.getName().endsWith(".json")) {
				try {
					if (checkQuest(f)) {
						Object q = null;
						JsonObject config = new JsonFileInterpretter(f).getJson();
						String name = config.get("Name").getAsString();
						String descString = config.get("Description").getAsString();
						boolean requireTurnIn = config.has("RequireTurnIn") ? config.get("RequireTurnIn").getAsBoolean()
								: true;
						String presentString = config.has("Present") ? config.get("Present").getAsString()
								: "Would you like to ACCEPT or DENY?";
						String denyString = config.has("Deny") ? config.get("Deny").getAsString()
								: "That's Very Sad Traveller";
						String acceptString = config.has("Accept") ? config.get("Accept").getAsString()
								: "Thank you Traveller!";
						String type = config.has("Type") ? config.get("Type").getAsString() : "Basic";
						List<String> questLore = new ArrayList<>();
						if(config.has("Lore") && config.get("Lore").isJsonArray()) {
							config.get("Lore").getAsJsonArray().forEach(e -> {
								questLore.add(e.getAsString());
							});
						}
						List<String> parentQuests = new ArrayList<>();
						if(config.has("Parents") && config.get("Parents").isJsonArray()) {
							config.get("Parents").getAsJsonArray().forEach(e -> {
								parentQuests.add(e.getAsString());
							});
						}

						// Load the quests based on their properties to get around an ugly if/else
						// if/else block like AbilityManager has
						q = (Quest) questTypes.get(type).clone();
						((Quest) q).loadQuest(config.get("Properties").getAsJsonObject());
						((Quest) q).setName(name);
						((Quest) q).setLoreForQuest(questLore);
						((Quest) q).setQuestType(type);
						((Quest) q).setRequireTurnIn(requireTurnIn);
						((Quest) q).setDescString(descString);
						((Quest) q).setAcceptString(acceptString);
						((Quest) q).setDenyString(denyString);
						((Quest) q).setPresentString(presentString);
						((Quest) q).setParentQuests(parentQuests);

						// Load any rewards if there are any
						if (config.has("Rewards")) {
							loadRewards(config.get("Rewards").getAsJsonObject(), (Quest) q);
						}

						allQuests.add((Quest) q);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}

	/**
	 * Used to register a new Quest AND quest type
	 * 
	 * @param type      Type of quest
	 * @param baseClass Quest to represent
	 */
	public void registerQuest(String type, Quest baseClass) {
		baseClass.setQuestType(type);
		questTypes.put(type.toUpperCase(), baseClass);
	}

	/**
	 * Get all parent quests of from the provided Quest
	 * 
	 * @param s Name of quest
	 * @return List of parent quests or null
	 */
	public List<String> getParentQuestsForQuest(String s) {
		Quest q = allQuests.stream().filter(e -> e.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
		if (q == null)
			return null;
		return q.getParentQuests();
	}

	/**
	 * Get the quest based on the quests name
	 * 
	 * @param s Name of Quest
	 * @return Quest or Null
	 */
	public Quest getQuest(String s) {
		return allQuests.stream().filter(e -> e.getName().equals(s)).findFirst().orElse(null);
	}

	private void loadRewards(JsonObject config, Quest q) {

		List<String> customItems = new ArrayList<>();
		if(config.has("Items") && config.get("Items").isJsonArray()) {
			config.get("Items").getAsJsonArray().forEach(e -> {
				customItems.add(e.getAsString());
			});
		}
		
		List<String> questRewards = new ArrayList<>();
		if(config.has("RewardQuests") && config.get("RewardQuests").isJsonArray()) {
			config.get("RewardQuests").getAsJsonArray().forEach(e -> {
				questRewards.add(e.getAsString());
			});
		}

		List<String> craftingRecipes = new ArrayList<>();
		if(config.has("CraftingRecipes") && config.get("CraftingRecipes").isJsonArray()) {
			config.get("CraftingRecipes").getAsJsonArray().forEach(e -> {
				craftingRecipes.add(e.getAsString());
			});
		}
		
		double xp = config.has("XP") ? config.get("XP").getAsDouble() : 0.0;
		double money = config.has("Money") ? config.get("Money").getAsDouble() : 0.0;

		q.setXp(xp);
		q.setMoney(money);
		q.setItems(customItems);
		q.setChildQuests(questRewards);
		q.setRecipes(craftingRecipes);
	}

	private boolean checkQuest(File f) throws IOException {
		boolean load = true;

		JsonObject config = new JsonFileInterpretter(f).getJson();

		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);

		if (!config.has("Name")) {
			wbw.write("[" + f.getName() + "] 'Name' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (!config.has("Description")) {
			wbw.write("[" + f.getName() + "] 'Description' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (!questTypes.containsKey(config.get("Type").getAsString().toUpperCase())) {
			wbw.write("[" + f.getName() + "] Has unknown type: " + config.get("Type").getAsString());
			wbw.newLine();
			load = false;
		}

		if (!config.has("Properties")) {
			wbw.write("[" + f.getName() + "] 'Properties' property is not set to load quest information!");
			wbw.newLine();
			load = false;
		}

		if ((Quest) questTypes.get(config.get("Type").getAsString().toUpperCase()).clone() == null) {
			wbw.write("[" + f.getName() + "] Unable to create new quest. Check clone() method!");
			wbw.newLine();
			load = false;
		}

		wbw.flush();
		wbw.close();
		return load;
	}

}