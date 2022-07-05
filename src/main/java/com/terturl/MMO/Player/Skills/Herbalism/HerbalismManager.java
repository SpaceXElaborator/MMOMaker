package com.terturl.MMO.Player.Skills.Herbalism;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.MMODropChance;
import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Math.IntMinMax;

import lombok.Getter;

/**
 * Manager class that will load in all gatherable blocks for the Herbalism skill
 * and assign the MMODropChance/IntMinMax value to the CustomItems for when a
 * player clicks on the required material to collect
 * 
 * @author Sean Rahman
 * @since 0.47.0
 *
 */
public class HerbalismManager {

	@Getter
	private List<HerbalismGatherItems> herbalismItems = new ArrayList<>();

	public HerbalismManager() {
		File skillsDir = new File(MinecraftMMO.getInstance().getDataFolder(), "skills");
		if (!skillsDir.exists())
			skillsDir.mkdir();
		File herbalism = new File(skillsDir, "Herbalism.json");
		if (!herbalism.exists())
			createHerbalismFile(herbalism);
		JsonObject config = new JsonFileInterpretter(herbalism).getJson();
		
		JsonArray blocks = config.get("Blocks").getAsJsonArray();
		for(JsonElement je : blocks) {
			if(!je.isJsonObject()) continue;
			JsonObject herbalismCall = je.getAsJsonObject();
			Material mat = Material.valueOf(herbalismCall.get("Material").toString().toUpperCase());
			Double xp = Double.parseDouble(herbalismCall.get("XP").toString());
			Map<CustomItem, MMODropChance> herbItems = new HashMap<>();
			
			if(herbalismCall.has("Items") && herbalismCall.get("Items").isJsonArray()) {
				JsonArray items = herbalismCall.get("Items").getAsJsonArray();
				for(JsonElement je2 : items) {
					if(!je2.isJsonObject()) continue;
					JsonObject item = je2.getAsJsonObject();
					CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(item.get("Item").toString());
					JsonObject minMax = item.get("Amount").getAsJsonObject();
					IntMinMax imm = new IntMinMax(minMax.get("Min").getAsInt(),
							minMax.get("Max").getAsInt());
					Double chance = item.get("Chance").getAsDouble();
					MMODropChance mdc = new MMODropChance(ci, imm, chance);
					herbItems.put(ci, mdc);
				}
			}
			
			HerbalismGatherItems hgi = new HerbalismGatherItems(mat, xp, herbItems);
			herbalismItems.add(hgi);
		}
		
	}

	/**
	 * Checks to make sure the Material is present as a herbalism gatherable material
	 * @param mat Material the player clicked on
	 * @return If it is in the list or not
	 */
	public boolean containsMaterial(Material mat) {
		for (HerbalismGatherItems hgi : herbalismItems) {
			if (hgi.getMat().equals(mat))
				return true;
		}
		return false;
	}

	/**
	 * Will return what can drop and their rate to based on the Material given
	 * @param mat Material the player clicked on
	 * @return HerbalismGatherItems that contains drop rate and CustomItems
	 */
	public HerbalismGatherItems getItem(Material mat) {
		for (HerbalismGatherItems hgi : herbalismItems) {
			if (hgi.getMat().equals(mat))
				return hgi;
		}
		return null;
	}

	private void createHerbalismFile(File f) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(f, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		pw.print("{\n");
		pw.print("  \"Blocks\": []\n");
		pw.print("}");
		pw.flush();
		pw.close();
	}

}