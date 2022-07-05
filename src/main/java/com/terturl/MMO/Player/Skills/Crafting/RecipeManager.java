package com.terturl.MMO.Player.Skills.Crafting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Items.CustomItemManager;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * Has all the methods to be able to create and save MMORecipes from JSON files
 * @author Sean Rahman
 * @see com.terturl.MMO.Player.Crafting.MMORecipe
 * @since 0.32.0
 *
 */
public class RecipeManager {

	@Getter
	private List<MMORecipe> recipes = new ArrayList<>();
	
	@Getter
	private File warnings;
	
	public RecipeManager() throws IOException {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Recipes...");
		File skillsDir = new File(MinecraftMMO.getInstance().getDataFolder(), "skills");
		if(!skillsDir.exists()) skillsDir.mkdir();
		
		File recDir = new File(skillsDir, "recipes");
		warnings = new File(recDir, "Warnings.txt");
		if(!recDir.exists()) recDir.mkdir();
		if(getWarnings().exists()) { getWarnings().delete(); }
		
		if(recDir.listFiles().length > 0) {
			for(File f : recDir.listFiles()) {
				if(f.getName().endsWith(".json")) {
					if(checkRecipe(f)) {
						JsonObject config = new JsonFileInterpretter(f).getJson();
						String item = config.get("Product").getAsString();
						int level = config.has("Level") ? config.get("Level").getAsInt() : 1;
						int amount = config.has("Amount") ? config.get("Amount").getAsInt() : 1;
						double xpToGive = config.has("XP") ? config.get("XP").getAsDouble() : 0.0;
						CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(item);
						String name = f.getName().substring(0, f.getName().length()-5);
						
						MMORecipe mr = new MMORecipe(name, ci, level);
						mr.setAmountToGive(amount);
						mr.setXpGiven(xpToGive);
						
						JsonArray ja = config.get("Items").getAsJsonArray();
						for(JsonElement je : ja) {
							if(!je.isJsonObject()) continue;
							JsonObject craftComponent = je.getAsJsonObject();
							String craftItem = craftComponent.get("Item").getAsString();
							int amountRequired = craftComponent.has("Amount") ? craftComponent.get("Amount").getAsInt() : 1;
							CustomItem component = MinecraftMMO.getInstance().getItemManager().getItem(craftItem);
							mr.addItem(component, amountRequired);
						}
						
						recipes.add(mr);
					}
				}
			}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}
	
	/**
	 * Add a recipe to the plugin for crafting information
	 * @param mr The MMORecipe to add to the manager
	 */
	public void addRecipe(MMORecipe mr) {
		if(recipes.contains(mr)) return;
		recipes.add(mr);
	}
	
	/**
	 * Obtain an MMORecipe by name from the recipes List
	 * @param s The name of an MMORecipe for searching
	 * @return The MMORecipe gotten from the name
	 */
	public MMORecipe getRecipeByName(String s) {
		return recipes.stream().filter(e -> e.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
	}
	
	private boolean checkRecipe(File f) throws IOException {
		boolean load = true;
		
		// Create a new json config derived from the file and create a new filewriter and bufferedwriter to write to the errors file
		JsonObject config = new JsonFileInterpretter(f).getJson();
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		String item = config.get("Product").getAsString();
		if(!cim.getCustomItems().containsKey(item)) {
			wbw.write("[" + f.getName() + "] 'Item' Does not contain a valid CustomItem " + item);
			wbw.newLine();
			load = false;
		}
		
		JsonArray ja = config.get("Items").getAsJsonArray();
		for(JsonElement je : ja) {
			if(!je.isJsonObject()) continue;
			JsonObject craftComponent = je.getAsJsonObject();
			String craftItem = craftComponent.get("Item").toString();
			if(!cim.getCustomItems().containsKey(craftItem)) {
				wbw.write("[" + f.getName() + "] Does not contain a valid CustomItem " + craftItem + " in Items Array");
				wbw.newLine();
				load = false;
			}
		}
		
		wbw.flush();
		wbw.close();
		return load;
	}
	
}