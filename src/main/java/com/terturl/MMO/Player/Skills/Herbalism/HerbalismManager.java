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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.MMODropChance;
import com.terturl.MMO.Util.Items.CustomItem;
import com.terturl.MMO.Util.Math.IntMinMax;

import lombok.Getter;

public class HerbalismManager {

	@Getter
	private List<HerbalismGatherItems> herbalismItems = new ArrayList<>();
	
	public HerbalismManager() {
		File skillsDir = new File(MinecraftMMO.getInstance().getDataFolder(), "skills");
		if(!skillsDir.exists()) skillsDir.mkdir();
		File herbalism = new File(skillsDir, "Herbalism.json");
		if(!herbalism.exists()) createHerbalismFile(herbalism);
		JsonFileInterpretter config = new JsonFileInterpretter(herbalism);
		JSONArray blocks = config.getArray("Blocks");
		for(Object o : blocks) {
			JSONObject herbalismCall = (JSONObject)o;
			Material mat = Material.valueOf(herbalismCall.get("Material").toString().toUpperCase());
			Double xp = Double.parseDouble(herbalismCall.get("XP").toString());
			Map<CustomItem, MMODropChance> herbItems = new HashMap<>();
			JSONArray items = (JSONArray)herbalismCall.get("Items");
			for(Object o2 : items) {
				JSONObject item = (JSONObject)o2;
				CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(item.get("Item").toString());
				JSONObject minMax = (JSONObject)item.get("Amount");
				IntMinMax imm = new IntMinMax(Integer.valueOf(minMax.get("Min").toString()), Integer.valueOf(minMax.get("Max").toString()));
				Double chance = Double.parseDouble(item.get("Chance").toString());
				MMODropChance mdc = new MMODropChance(ci, imm, chance);
				herbItems.put(ci, mdc);
			}
			HerbalismGatherItems hgi = new HerbalismGatherItems(mat, xp, herbItems);
			herbalismItems.add(hgi);
		}
	}
	
	public boolean containsMaterial(Material mat) {
		for(HerbalismGatherItems hgi : herbalismItems) {
			if(hgi.getMat().equals(mat)) return true;
		}
		return false;
	}
	
	public HerbalismGatherItems getItem(Material mat) {
		for(HerbalismGatherItems hgi : herbalismItems) {
			if(hgi.getMat().equals(mat)) return hgi;
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