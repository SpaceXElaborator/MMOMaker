package com.terturl.MMO.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Entity.NPC.NPC.ItemSlot;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Player.Skills.Crafting.CraftingSkill;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.BasicInventoryItems;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.CustomItemManager;
import com.terturl.MMO.Util.JSONHelpers.ItemUtils;
import com.terturl.MMO.Util.JSONHelpers.LocationUtils;

import lombok.Getter;

public class PlayerHandler {
	
	@Getter
	private List<MMOPlayer> players = new ArrayList<>();
	
	@Getter
	private Map<MMOPlayer, List<NPC>> playerNPCs = new HashMap<>();
	private File playersFolder;
	
	public PlayerHandler() {
		playersFolder = new File(MinecraftMMO.getInstance().getDataFolder(), "players");
		if(!playersFolder.exists()) playersFolder.mkdir();
	}
	
	public MMOPlayer getProjectile(Projectile p) {
		return players.stream().filter(e -> e.getProjectileMapping().containsKey(p.getUniqueId())).findFirst().orElse(null);
	}
	
	public void addQuestItem(Player p) {
		p.getInventory().setItem(8, new ItemStack(Material.COMPASS));
		p.updateInventory();
	}
	
	public boolean PlayerExists(Player p) {
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		if(f.exists()) return true;
		return false;
	}
	
	public void addPlayer(MMOPlayer mp) {
		players.add(mp);
	}
	
	public void removePlayer(Player p) {
		int removalInt = -1;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getPlayerUUID().equals(p.getUniqueId())) removalInt = i;
		}
		if(removalInt == -1) return;
		players.remove(removalInt);
	}
	
	public void giveBasicItems(Player p) {
		p.getInventory().setItem(8, new ItemStack(Material.COMPASS));
		p.getInventory().setItem(9, BasicInventoryItems.getPlayerClassItem(p));
		p.getInventory().setItem(2, BasicInventoryItems.getQuickActionAbilities());
		p.getInventory().setItem(3, BasicInventoryItems.getQuickActionAbilities());
		p.getInventory().setItem(4, BasicInventoryItems.getQuickActionAbilities());
		p.getInventory().setItem(5, BasicInventoryItems.getRegularAbilities());
		p.getInventory().setItem(6, BasicInventoryItems.getRegularAbilities());
		p.getInventory().setItem(7, BasicInventoryItems.getRegularAbilities());
		p.updateInventory();
	}
	
	public void pickClass(Player p, Integer i) {
		MMOPlayer mp = getPlayer(p);
		mp.setCurrentCharacter(i);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		p.getInventory().setHelmet(cim.getItem(mc.getHelmet()).makeItem());
		p.getInventory().setChestplate(cim.getItem(mc.getChest()).makeItem());
		p.getInventory().setLeggings(cim.getItem(mc.getLegs()).makeItem());
		p.getInventory().setBoots(cim.getItem(mc.getBoots()).makeItem());
		p.getInventory().setItem(0, cim.getItem(mc.getMainH()).makeItem());
		p.getInventory().setItem(1, cim.getItem(mc.getOffH()).makeItem());
		mp.getPlayer().teleport(mc.getClassLocation());
		giveBasicItems(p);
		Bukkit.getScheduler().runTaskLater(MinecraftMMO.getInstance(), new Runnable() {
			public void run() {
				mp.updateNPCQuests();
			}
		}, 5);
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlayer(Player p) {
		p.getInventory().clear();
		p.updateInventory();
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		MMOPlayer mp = new MMOPlayer(p);
		JsonFileInterpretter config = new JsonFileInterpretter(f);
		JSONObject jo = config.getObject("Classes");
		jo.forEach((k, v) -> {
			JSONObject clazz = (JSONObject) jo.get(k.toString());
			Integer level = Integer.valueOf(clazz.get("Level").toString());
			Location loc = LocationUtils.locationDeSerializer(clazz.get("Location").toString());
			Double mon = Double.valueOf(clazz.get("Currency").toString());
			Double xp = Double.valueOf(clazz.get("XP").toString());
			MMOClass mc = (MMOClass) MinecraftMMO.getInstance().getClassHandler().getClass(clazz.get("Class").toString()).clone();
			mc.setMoney(mon);
			mc.setLevel(level);
			mc.setClassLocation(loc);
			mc.setXp(xp);
			
			JSONObject craftSkill = (JSONObject)clazz.get("CraftingSkill");
			mc.getCraftSkill().setLevel(Integer.valueOf(craftSkill.get("Level").toString()));
			mc.getCraftSkill().setXp(Double.parseDouble(craftSkill.get("XP").toString()));
			
			JSONArray craftingRecipes = (JSONArray)clazz.get("CraftingRecipes");
			craftingRecipes.forEach(e -> {
				String recipe = e.toString();
				if(MinecraftMMO.getInstance().getRecipeManager().getRecipeByName(recipe) == null) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, recipe + " Does not exist in player's " + p.getName() + " save file with UUID: " + p.getUniqueId().toString());
					return;
				}
				if(mc.getCraftingRecipes().contains(recipe)) return;
				mc.getCraftingRecipes().add(recipe);
			});
			
			JSONArray playerAbilities = (JSONArray)clazz.get("PlayerAbilities");
			playerAbilities.forEach(e -> {
				String ability = e.toString();
				if(!MinecraftMMO.getInstance().getAbilityManager().getAbilities().containsKey(ability)) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, ability + " Does not exist in player's " + p.getName() + " save file with UUID: " + p.getUniqueId().toString());
					return;
				}
				if(mc.getPlayerAbilities().contains(ability)) return;
				mc.getPlayerAbilities().add(ability);
			});
			
			JSONArray active = (JSONArray)clazz.get("Active");
			active.forEach(e -> {
				JSONObject inProg = (JSONObject)e;
				if(mc.getActiveQuests().contains(MinecraftMMO.getInstance().getQuestManager().getQuest(inProg.get("Name").toString()))) return;
				Object q = MinecraftMMO.getInstance().getQuestManager().getQuest(inProg.get("Name").toString()).clone();
				
				if(inProg.containsKey("Properties")) {
					JSONObject questProperties = (JSONObject)inProg.get("Properties");
					((Quest) q).loadQuestToPlayer(questProperties);
				}
				
				mc.getActiveQuests().add((Quest) q);
			});
			JSONArray completable = (JSONArray)clazz.get("Completable");
			completable.forEach(e -> {
				if(mc.getCompletedableQuests().contains(MinecraftMMO.getInstance().getQuestManager().getQuest(e.toString()))) return;
				mc.getCompletedableQuests().add(MinecraftMMO.getInstance().getQuestManager().getQuest(e.toString()));
			});
			JSONArray completed = (JSONArray)clazz.get("Completed");
			completed.forEach(e -> {
				if(mc.getCompletedQuests().contains(e.toString())) return;
				mc.getCompletedQuests().add(e.toString());
			});
			JSONObject inv = (JSONObject) clazz.get("Inventory");
			
			mc.setHelmet(ItemUtils.JSONToItem((JSONObject) inv.get("Helmet")).getName());
			mc.setChest(ItemUtils.JSONToItem((JSONObject) inv.get("Chestplate")).getName());
			mc.setLegs(ItemUtils.JSONToItem((JSONObject) inv.get("Leggings")).getName());
			mc.setBoots(ItemUtils.JSONToItem((JSONObject) inv.get("Boots")).getName());
			mc.setMainH(ItemUtils.JSONToItem((JSONObject) inv.get("MainHand")).getName());
			mc.setOffH(ItemUtils.JSONToItem((JSONObject) inv.get("OffHand")).getName());
			
			mp.getMmoClasses().add(mc);
		});
		
		int i = 0;
		List<NPC> playerOnlyNPCs = new ArrayList<>();
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		for(MMOClass mc : mp.getMmoClasses()) {
			NPC npc = new NPC(MinecraftMMO.getInstance().getMmoConfiguration().getClassSpawnLocations().get(i), "Character " + String.valueOf(i));
			npc.spawnNPC(p);
			npc.setHeldClass(mc);
			npc.setEquipment(p, ItemSlot.HELMET, cim.getItem(mc.getHelmet()).makeItem());
			npc.setEquipment(p, ItemSlot.CHESTPLATE, cim.getItem(mc.getChest()).makeItem());
			npc.setEquipment(p, ItemSlot.LEGGINGS, cim.getItem(mc.getLegs()).makeItem());
			npc.setEquipment(p, ItemSlot.BOOTS, cim.getItem(mc.getBoots()).makeItem());
			npc.setEquipment(p, ItemSlot.MAIN_HAND, cim.getItem(mc.getMainH()).makeItem());
			npc.setEquipment(p, ItemSlot.OFF_HAND, cim.getItem(mc.getOffH()).makeItem());
			playerOnlyNPCs.add(npc);
			npc.lookAtPlayer(p, p);
			i++;
		}
		
		playerNPCs.put(mp, playerOnlyNPCs);
		
		// mp.setCurrentCharacter(0);
		addPlayer(mp);
	}
	
	public boolean isPlayerClassNPC(Player p, Integer id) {
		MMOPlayer mp = getPlayer(p);
		for(NPC npc : playerNPCs.get(mp)) {
			if(Objects.equal(npc.getEntityID(), id)) {
				return true;
			}
		}
		return false;
	}
	
	public NPC getPlayerClassNPC(Player p, Integer id) {
		MMOPlayer mp = getPlayer(p);
		for(NPC npc : playerNPCs.get(mp)) {
			if(Objects.equal(npc.getEntityID(), id)) {
				return npc;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void savePlayerInfo(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if(mp.getCurrentCharacter() != -1) {
			File f = new File(playersFolder, p.getUniqueId() + ".json");
			if(f.exists()) f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JSONObject jo = new JSONObject();
			JSONObject classes = new JSONObject();
			int i = 1;
			// TODO: Fix this on a per class basis. Since right now all classes with have the exact same item all the time
			for(MMOClass mc : mp.getMmoClasses()) {
				if(mc.equals(mp.getMmoClasses().get(mp.getCurrentCharacter()))) {
					JSONObject clazz = new JSONObject();
					String s = mc.getName();
					String loc = LocationUtils.locationSerializer(mc.getClassLocation());
					Integer level = mc.getLevel();
					Double mon = mc.getMoney();
					Double xp = mc.getXp();
					CraftingSkill ck = mc.getCraftSkill();
					
					JSONArray craftingRecipes = new JSONArray();
					for(String craftingRecipe : mc.getCraftingRecipes()) {
						if(craftingRecipes.contains(craftingRecipe)) continue;
						craftingRecipes.add(craftingRecipe);
					}
					
					JSONArray playerAbilities = new JSONArray();
					for(String ability : mc.getPlayerAbilities()) {
						if(playerAbilities.contains(ability)) continue;
						playerAbilities.add(ability);
					}
					
					JSONArray completed = new JSONArray();
					for(String completedName : mc.getCompletedQuests()) {
						if(completed.contains(completedName)) continue;
						completed.add(completedName);
					}
					
					JSONArray completable = new JSONArray();
					for(Quest q : mc.getCompletedableQuests()) {
						if(completable.contains(q.getName())) continue;
						completable.add(q.getName());
					}
					
					JSONArray inProg = new JSONArray();
					for(Quest q : mc.getActiveQuests()) {
						if(inProg.contains(q.getName())) continue;
						JSONObject questJO = new JSONObject();
						questJO.put("Name", q.getName());
						JSONObject questProperties = q.saveQuest();
						if(questProperties != null) questJO.put("Properties", q.saveQuest());
						inProg.add(questJO);
					}
					
					JSONObject craftSkill = new JSONObject();
					craftSkill.put("Level", ck.getLevel());
					craftSkill.put("XP", ck.getXp());
					
					clazz.put("Class", s);
					clazz.put("Level", level);
					clazz.put("XP", xp);
					clazz.put("Currency", mon);
					clazz.put("Location", loc);
					clazz.put("CraftingSkill", craftSkill);
					clazz.put("CraftingRecipes", craftingRecipes);
					clazz.put("PlayerAbilities", playerAbilities);
					clazz.put("Completed", completed);
					clazz.put("Completable", completable);
					clazz.put("Active", inProg);
					
					JSONObject inv = new JSONObject();
					CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
					inv.put("Helmet", (p.getInventory().getHelmet() != null) 
							? ItemUtils.itemToJSON(cim.getItem(mc.getHelmet())) 
							: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
					inv.put("Chestplate", (p.getInventory().getChestplate() != null) 
							? ItemUtils.itemToJSON(cim.getItem(mc.getChest())) 
							: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
					inv.put("Leggings", (p.getInventory().getLeggings() != null) 
							? ItemUtils.itemToJSON(cim.getItem(mc.getLegs())) 
							: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
					inv.put("Boots", (p.getInventory().getBoots() != null) 
							? ItemUtils.itemToJSON(cim.getItem(mc.getBoots())) 
							: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
					inv.put("MainHand", (p.getInventory().getItem(0) != null) 
							? ItemUtils.itemToJSON(cim.getItem(mc.getMainH())) 
							: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
					inv.put("OffHand", (p.getInventory().getItem(1) != null) 
							? ItemUtils.itemToJSON(cim.getItem(mc.getOffH())) 
							: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
					clazz.put("Inventory", inv);
					
					classes.put("Class" + String.valueOf(i), clazz);
					i++;
				}
			}
			jo.put("Classes", classes);
			TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
			treeMap.putAll(jo);
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = g.toJson(treeMap);
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(prettyJson);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		removePlayer(p);
	}
	
	public void createPlayerFile(Player p) {
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		if(f.exists()) return;
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintWriter pw = new PrintWriter(f, "UTF-8");
			pw.print("{");
			pw.print("}");
			pw.flush();
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public MMOPlayer getPlayer(Player p) {
		return players.stream().filter(e -> e.getPlayerUUID().equals(p.getUniqueId())).findFirst().orElse(null);
	}
	
}