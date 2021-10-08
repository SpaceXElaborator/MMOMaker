package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class CustomCraftQuest extends Quest {

	@Getter @Setter
	private Map<String, Integer> toCraft = new HashMap<>();
	
	@Getter @Setter
	private Map<String, Integer> hasCrafted = new HashMap<>();
	
	@Override
	public Object clone() {
		CustomCraftQuest q = new CustomCraftQuest();
		q.setName(getName());
		q.setAcceptString(getAcceptString());
		q.setChildQuests(getChildQuests());
		q.setDenyString(getDenyString());
		q.setDescString(getDescString());
		q.setItems(getItems());
		q.setMoney(getMoney());
		q.setXp(getXp());
		q.setParentQuests(getParentQuests());
		q.setPresentString(getPresentString());
		q.setToCraft(toCraft);
		q.setHasCrafted(hasCrafted);
		return q;
	}
	
	public void addItemToCraft(String s, Integer a) {
		toCraft.put(s, a);
		hasCrafted.put(s, 0);
	}

	@Override
	public boolean hasComplete(Player p) {
		for(String s : toCraft.keySet()) {
			if(toCraft.get(s) != hasCrafted.get(s)) return false;
		}
		return true;
	}

	@Override
	public void completeQuest(Player p) {
		giveRewards(p);
		p.sendMessage("Completed quest");
	}

	@Override
	public ItemStack questItem(Player p) {
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta meta = is.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + getName());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Requirements: ");
		for(String s : toCraft.keySet()) {
			lore.add("Craft " + String.valueOf(toCraft.get(s)) + " " + s);
		}
		lore.add(ChatColor.GREEN + "\nCompleted: ");
		for(String s : hasCrafted.keySet()) {
			lore.add("Has Crafted " + String.valueOf(hasCrafted.get(s)) + " " + s);
		}
		meta.setLore(lore);
		is.setItemMeta(meta);
		
		return is;
	}

	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("CraftingInformation");
		for(Object o : ja) {
			JSONObject entry = (JSONObject)o;
			String s = entry.get("Item").toString();
			Integer amount = Integer.parseInt(entry.get("Amount").toString());
			addItemToCraft(s, amount);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasCrafted = new JSONArray();
		for(String s : getHasCrafted().keySet()) {
			JSONObject entry = new JSONObject();
			entry.put("Item", s);
			entry.put("Amount", getHasCrafted().get(s));
			hasCrafted.add(entry);
		}
		jo.put("Crafted", hasCrafted);
		return jo;
	}

	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if(jo.containsKey("Crafted")) {
			JSONArray entries = (JSONArray)jo.get("Crafted");
			for(Object o : entries) {
				JSONObject entity = (JSONObject)o;
				String et = entity.get("Item").toString();
				Integer amount = Integer.parseInt(entity.get("Amount").toString());
				getHasCrafted().put(et, amount);
			}
		}
	}
	
}