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
import org.json.simple.JSONObject;

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
						JsonFileInterpretter config = new JsonFileInterpretter(f);
						String name = config.getString("Name");
						String descString = config.getString("Description");
						boolean requireTurnIn = config.contains("RequireTurnIn") ? config.getBoolean("RequireTurnIn")
								: true;
						String presentString = config.contains("Present") ? config.getString("Present")
								: "Would you like to ACCEPT or DENY?";
						String denyString = config.contains("Deny") ? config.getString("Deny")
								: "That's Very Sad Traveller";
						String acceptString = config.contains("Accept") ? config.getString("Accept")
								: "Thank you Traveller!";
						String type = config.contains("Type") ? config.getString("Type") : "Basic";
						List<String> questLore = config.contains("Lore") ? config.getStringList("Lore")
								: new ArrayList<>();
						List<String> parentQuests = config.contains("Parents") ? config.getStringList("Parents")
								: new ArrayList<>();

						// Load the quests based on their properties to get around an ugly if/else
						// if/else block like AbilityManager has
						q = (Quest) questTypes.get(type).clone();
						((Quest) q).loadQuest(config.getObject("Properties"));
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
						if (config.contains("Rewards")) {
							loadRewards(config.getObject("Rewards"), (Quest) q);
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
		questTypes.put(type, baseClass);
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

	private void loadRewards(JSONObject jo, Quest q) {
		JsonFileInterpretter config = new JsonFileInterpretter(jo);

		List<String> customItems = config.contains("Items") ? config.getStringList("Items") : new ArrayList<>();
		List<String> questRewards = config.contains("RewardQuests") ? config.getStringList("RewardQuests")
				: new ArrayList<>();
		List<String> craftingRecipes = config.contains("CraftingRecipes") ? config.getStringList("CraftingRecipes")
				: new ArrayList<>();
		double xp = config.contains("XP") ? config.getDouble("XP") : 0.0;
		double money = config.contains("Money") ? config.getDouble("Money") : 0.0;

		q.setXp(xp);
		q.setMoney(money);
		q.setItems(customItems);
		q.setChildQuests(questRewards);
		q.setRecipes(craftingRecipes);
	}

	private boolean checkQuest(File f) throws IOException {
		boolean load = true;

		JsonFileInterpretter config = new JsonFileInterpretter(f);

		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);

		if (!config.contains("Name")) {
			wbw.write("[" + f.getName() + "] 'Name' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (!config.contains("Description")) {
			wbw.write("[" + f.getName() + "] 'Description' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (!questTypes.containsKey(config.getString("Type"))) {
			wbw.write("[" + f.getName() + "] Has unknown type: " + config.getString("Type"));
			wbw.newLine();
			load = false;
		}

		if (!config.contains("Properties")) {
			wbw.write("[" + f.getName() + "] 'Properties' property is not set to load quest information!");
			wbw.newLine();
			load = false;
		}

		if ((Quest) questTypes.get(config.getString("Type")).clone() == null) {
			wbw.write("[" + f.getName() + "] Unable to create new quest. Check clone() method!");
			wbw.newLine();
			load = false;
		}

		wbw.flush();
		wbw.close();
		return load;
	}

}