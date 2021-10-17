package com.terturl.MMO.Player.MMOClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.CustomItem.SlotType;
import com.terturl.MMO.Util.Items.CustomItemManager;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class ClassHandler {

	// TODO: Make sure items that are put in their slots are actually items that pertain to that SlotType
	
	@Getter
	private List<MMOClass> classes = new ArrayList<>();
	
	@Getter
	private File warnings;
	
	public ClassHandler() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Classes...");
		File classDir = new File(MinecraftMMO.getInstance().getDataFolder(), "classes");
		if(!classDir.exists()) classDir.mkdir();
		warnings = new File(classDir, "Warnings.txt");
		if(getWarnings().exists()) { getWarnings().delete(); }
		
		try {
			getWarnings().createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileWriter wfw = new FileWriter(getWarnings());
			BufferedWriter wbw = new BufferedWriter(wfw);
			if(classDir.listFiles().length > 0) {
				for(File f : classDir.listFiles()) {
					if(f.getName().endsWith(".json")) {
						if(checkStarterValues(f)) {
							JsonFileInterpretter config = new JsonFileInterpretter(f);
							String name = config.getString("Name");
							Map<SlotType, String> starterItemsMap = new HashMap<>();
							JSONObject starterItems = config.getObject("StarterItems");
							
							if(containsClass(name)) {
								wbw.write("[" + f.getName() + "] Class: " + name + " Exists");
								wbw.newLine();
								continue;
							}
							
							if(checkStarterItems(starterItems)) {
								starterItemsMap = setStarterItemMap(starterItems);
							}
							
							MMOClass mc = new MMOClass(name);
							mc.setStartItems(starterItemsMap);
							mc.setHelmet(starterItemsMap.get(SlotType.HELMET));
							mc.setChest(starterItemsMap.get(SlotType.CHEST));
							mc.setLegs(starterItemsMap.get(SlotType.LEGS));
							mc.setBoots(starterItemsMap.get(SlotType.BOOTS));
							mc.setMainH(starterItemsMap.get(SlotType.MAIN_HAND));
							mc.setOffH(starterItemsMap.get(SlotType.OFF_HAND));
							classes.add(mc);
						}
					}
				}
			}
			wbw.flush();
			wbw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}
	
	private Boolean checkStarterValues(File f) throws IOException {
		boolean load = true;
		JsonFileInterpretter config = new JsonFileInterpretter(f);
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		if(!config.contains("Name")) {
			wbw.write("[" + f.getName() + "] Does not contain 'Name'");
			wbw.newLine();
			load = false;
		}
		
		if(!config.contains("StarterItems")) {
			wbw.write("[" + f.getName() + "] Does not contain JSON object 'StartItems'");
			wbw.newLine();
			load = false;
		}
		
		wbw.flush();
		wbw.close();
		return load;
	}
	
	private Boolean checkStarterItems(JSONObject jo) throws IOException {
		boolean load = true;
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		
		for(Object o : jo.values()) {
			String item = o.toString();
			if(!cim.getCustomItems().containsKey(item)) {
				wbw.write(item + " Is not a valid CustomItem");
				wbw.newLine();
				load = false;
			}
		}
		
		wbw.flush();
		wbw.close();
		return load;
	}
	
	private Map<SlotType, String> setStarterItemMap(JSONObject jo) {
		Map<SlotType, String> map = new HashMap<SlotType, String>();
		
		if(jo.containsKey("Helmet")) {
			map.put(SlotType.HELMET, jo.get("Helmet").toString());
		} else {
			map.put(SlotType.HELMET, "MMO_ITEM_EMPTY_SLOT_ITEM");
		}
		
		if(jo.containsKey("Chestplate")) {
			map.put(SlotType.CHEST, jo.get("Chestplate").toString());
		} else {
			map.put(SlotType.CHEST, "MMO_ITEM_EMPTY_SLOT_ITEM");
		}
		
		if(jo.containsKey("Leggings")) {
			map.put(SlotType.LEGS, jo.get("Leggings").toString());
		} else {
			map.put(SlotType.LEGS, "MMO_ITEM_EMPTY_SLOT_ITEM");
		}
		
		if(jo.containsKey("Boots")) {
			map.put(SlotType.BOOTS, jo.get("Boots").toString());
		} else {
			map.put(SlotType.BOOTS, "MMO_ITEM_EMPTY_SLOT_ITEM");
		}
		
		if(jo.containsKey("MainHand")) {
			map.put(SlotType.MAIN_HAND, jo.get("MainHand").toString());
		} else {
			map.put(SlotType.MAIN_HAND, "MMO_ITEM_EMPTY_SLOT_ITEM");
		}
		
		if(jo.containsKey("OffHand")) {
			map.put(SlotType.OFF_HAND, jo.get("OffHand").toString());
		} else {
			map.put(SlotType.OFF_HAND, "MMO_ITEM_EMPTY_SLOT_ITEM");
		}
		
		return map;
	}
	
	public void registerClass(MMOClass cl) {
		for(MMOClass mc : classes) {
			if(mc.equals(cl)) return;
		}
		classes.add(cl);
	}
	
	public void addQuest(Player p, Quest q) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		mc.getActiveQuests().add(q);
		mp.updateNPCQuests();
	}
	
	public void selectClass(Player p, String mmoClass) {
		if (!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(p)) {
			MMOPlayer mp = new MMOPlayer(p);
			MMOClass mc = (MMOClass) getClass(mmoClass).clone();
			CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
			mp.getMmoClasses().add(mc);
			mp.setCurrentCharacter(mp.getMmoClasses().size()-1);
			MinecraftMMO.getInstance().getPlayerHandler().addPlayer(mp);
			p.getInventory().clear();
			p.getInventory().setBoots(cim.getCustomItems().get(mc.getStartItems().get(SlotType.BOOTS)).makeItem());
			p.getInventory().setChestplate(cim.getCustomItems().get(mc.getStartItems().get(SlotType.CHEST)).makeItem());
			p.getInventory().setLeggings(cim.getCustomItems().get(mc.getStartItems().get(SlotType.LEGS)).makeItem());
			p.getInventory().setHelmet(cim.getCustomItems().get(mc.getStartItems().get(SlotType.HELMET)).makeItem());
			p.getInventory().setItemInOffHand(cim.getCustomItems().get(mc.getStartItems().get(SlotType.OFF_HAND)).makeItem());
			p.getInventory().setItem(0, cim.getCustomItems().get(mc.getStartItems().get(SlotType.MAIN_HAND)).makeItem());
			p.getInventory().setItem(1, cim.getCustomItems().get(mc.getStartItems().get(SlotType.OFF_HAND)).makeItem());
			p.updateInventory();
		}
	}
	
	public MMOClass getClass(String name) {
		return classes.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
	}
	
	public boolean containsClass(String s) {
		MMOClass mmoClass = classes.stream().filter(e -> e.getName().equals(s)).findFirst().orElse(null);
		if(mmoClass == null) return false;
		return true;
	}
	
}