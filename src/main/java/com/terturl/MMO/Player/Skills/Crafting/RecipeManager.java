package com.terturl.MMO.Player.Skills.Crafting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Items.CustomItemManager;

import lombok.Getter;

public class RecipeManager {

	@Getter
	private List<MMORecipe> recipes = new ArrayList<>();
	
	@Getter
	private File warnings;
	
	public RecipeManager() throws IOException {
		File recDir = new File(MinecraftMMO.getInstance().getDataFolder(), "recipes");
		warnings = new File(recDir, "Warnings.txt");
		if(!recDir.exists()) recDir.mkdir();
		if(getWarnings().exists()) { getWarnings().delete(); }
		
		if(recDir.listFiles().length > 0) {
			for(File f : recDir.listFiles()) {
				if(f.getName().endsWith(".json")) {
					if(checkRecipe(f)) {
						JsonFileInterpretter config = new JsonFileInterpretter(f);
						String item = config.getString("Product");
						Integer level = config.contains("Level") ? config.getInt("Level") : 1;
						Integer amount = config.contains("Amount") ? config.getInt("Amount") : 1;
						Double xpToGive = config.contains("XP") ? config.getDouble("XP") : 0.0;
						CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(item);
						
						MMORecipe mr = new MMORecipe(ci, level);
						mr.setAmountToGive(amount);
						mr.setXpGiven(xpToGive);
						
						JSONArray ja = config.getArray("Items");
						for(Object o : ja) {
							JSONObject craftComponent = (JSONObject)o;
							String craftItem = craftComponent.get("Item").toString();
							Integer amountRequired = craftComponent.containsKey("Amount") ? Integer.valueOf(craftComponent.get("Amount").toString()) : 1;
							CustomItem component = MinecraftMMO.getInstance().getItemManager().getItem(craftItem);
							mr.addItem(component, amountRequired);
						}
						
						recipes.add(mr);
					}
				}
			}
		}
	}
	
	public void addRecipe(MMORecipe mr) {
		if(recipes.contains(mr)) return;
		recipes.add(mr);
	}
	
	private boolean checkRecipe(File f) throws IOException {
		boolean load = true;
		
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