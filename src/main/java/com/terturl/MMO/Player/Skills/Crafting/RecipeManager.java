package com.terturl.MMO.Player.Skills.Crafting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
						JsonFileInterpretter config = new JsonFileInterpretter(f);
						String item = config.getString("Product");
						int level = config.contains("Level") ? config.getInt("Level") : 1;
						int amount = config.contains("Amount") ? config.getInt("Amount") : 1;
						double xpToGive = config.contains("XP") ? config.getDouble("XP") : 0.0;
						CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(item);
						String name = f.getName().substring(0, f.getName().length()-5);
						
						MMORecipe mr = new MMORecipe(name, ci, level);
						mr.setAmountToGive(amount);
						mr.setXpGiven(xpToGive);
						
						JSONArray ja = config.getArray("Items");
						for(Object o : ja) {
							JSONObject craftComponent = (JSONObject)o;
							String craftItem = craftComponent.get("Item").toString();
							int amountRequired = craftComponent.containsKey("Amount") ? Integer.valueOf(craftComponent.get("Amount").toString()) : 1;
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
		JsonFileInterpretter config = new JsonFileInterpretter(f);
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		String item = config.getString("Product");
		if(!cim.getCustomItems().containsKey(item)) {
			wbw.write("[" + f.getName() + "] 'Item' Does not contain a valid CustomItem " + item);
			wbw.newLine();
			load = false;
		}
		
		JSONArray ja = config.getArray("Items");
		for(Object o : ja) {
			JSONObject craftComponent = (JSONObject)o;
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