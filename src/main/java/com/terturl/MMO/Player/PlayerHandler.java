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
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.ItemUtils;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.LocationUtils;
import com.terturl.MMO.Util.NPC;
import com.terturl.MMO.Util.NPC.ItemSlot;

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
		p.getInventory().setItem(8, new ItemStack(Material.BOOK));
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
	
	public void pickClass(Player p, Integer i) {
		MMOPlayer mp = getPlayer(p);
		mp.setCurrentCharacter(i);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		p.getInventory().setHelmet(mc.getHelmet());
		p.getInventory().setChestplate(mc.getChest());
		p.getInventory().setLeggings(mc.getLegs());
		p.getInventory().setBoots(mc.getBoots());
		p.getInventory().setItem(0, mc.getMainH());
		p.getInventory().setItem(1, mc.getOffH());
		mp.getPlayer().teleport(mc.getClassLocation());
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
			JSONArray active = (JSONArray)clazz.get("Active");
			active.forEach(e -> {
				if(mc.getActiveQuests().contains(MinecraftMMO.getInstance().getQuestManager().getQuest(e.toString()))) return;
				mc.getActiveQuests().add(MinecraftMMO.getInstance().getQuestManager().getQuest(e.toString()));
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
			
			mc.setHelmet(ItemUtils.JSONToItem((JSONObject) inv.get("Helmet")));
			mc.setChest(ItemUtils.JSONToItem((JSONObject) inv.get("Chestplate")));
			mc.setLegs(ItemUtils.JSONToItem((JSONObject) inv.get("Leggings")));
			mc.setBoots(ItemUtils.JSONToItem((JSONObject) inv.get("Boots")));
			mc.setMainH(ItemUtils.JSONToItem((JSONObject) inv.get("MainHand")));
			mc.setOffH(ItemUtils.JSONToItem((JSONObject) inv.get("OffHand")));
			
			mp.getMmoClasses().add(mc);
		});
		
		int i = 0;
		List<NPC> playerOnlyNPCs = new ArrayList<>();
		for(MMOClass mc : mp.getMmoClasses()) {
			NPC npc = new NPC(MinecraftMMO.getInstance().getMmoConfiguration().getClassSpawnLocations().get(i), "Character " + String.valueOf(i));
			npc.spawnNPC(p);
			npc.setHeldClass(mc);
			npc.setEquipment(p, ItemSlot.HELMET, mc.getHelmet());
			npc.setEquipment(p, ItemSlot.CHESTPLATE, mc.getChest());
			npc.setEquipment(p, ItemSlot.LEGGINGS, mc.getLegs());
			npc.setEquipment(p, ItemSlot.BOOTS, mc.getBoots());
			npc.setEquipment(p, ItemSlot.MAIN_HAND, mc.getMainH());
			npc.setEquipment(p, ItemSlot.OFF_HAND, mc.getOffH());
			playerOnlyNPCs.add(npc);
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
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		if(f.exists()) f.delete();
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if(mp == null) {
			f.delete();
			return;
		}
		
		JSONObject jo = new JSONObject();
		JSONObject classes = new JSONObject();
		int i = 1;
		// TODO: Fix this on a per class basis. Since right now all classes with have the exact same item all the time
		for(MMOClass mc : mp.getMmoClasses()) {
			JSONObject clazz = new JSONObject();
			String s = mc.getName();
			String loc = LocationUtils.locationSerializer(mc.getClassLocation());
			Integer level = mc.getLevel();
			Double mon = mc.getMoney();
			Double xp = mc.getXp();
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
				inProg.add(q.getName());
			}
			clazz.put("Class", s);
			clazz.put("Level", level);
			clazz.put("XP", xp);
			clazz.put("Currency", mon);
			clazz.put("Location", loc);
			clazz.put("Completed", completed);
			clazz.put("Completable", completable);
			clazz.put("Active", inProg);
			
			JSONObject inv = new JSONObject();
			inv.put("Helmet", ItemUtils.itemToJSON(p.getInventory().getHelmet()));
			inv.put("Chestplate", ItemUtils.itemToJSON(p.getInventory().getChestplate()));
			inv.put("Leggings", ItemUtils.itemToJSON(p.getInventory().getLeggings()));
			inv.put("Boots", ItemUtils.itemToJSON(p.getInventory().getBoots()));
			inv.put("MainHand", ItemUtils.itemToJSON(p.getInventory().getItem(0)));
			inv.put("OffHand", ItemUtils.itemToJSON(p.getInventory().getItem(1)));
			clazz.put("Inventory", inv);
			
			classes.put("Class" + String.valueOf(i), clazz);
			i++;
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