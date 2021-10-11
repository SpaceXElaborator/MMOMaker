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

public class MMOItemCollectQuest extends Quest {

	@Getter @Setter
	public Map<String, Integer> amountToCollect = new HashMap<>();
	@Getter @Setter
	public Map<String, Integer> hasCollected = new HashMap<>();
	
	public void addItemToCollect(String ci, Integer amount) {
		amountToCollect.put(ci, amount);
		hasCollected.put(ci, 0);
	}
	
	@Override
	public Object clone() {
		MMOItemCollectQuest q = new MMOItemCollectQuest();
		q.setName(getName());
		q.setQuestType(getQuestType());
		q.setAcceptString(getAcceptString());
		q.setChildQuests(getChildQuests());
		q.setDenyString(getDenyString());
		q.setDescString(getDescString());
		q.setItems(getItems());
		q.setMoney(getMoney());
		q.setXp(getXp());
		q.setParentQuests(getParentQuests());
		q.setPresentString(getPresentString());
		q.setAmountToCollect(getAmountToCollect());
		return q;
	}

	@Override
	public boolean hasComplete(Player p) {
		for(String ci : amountToCollect.keySet()) {
			if(amountToCollect.get(ci) != hasCollected.get(ci)) return false;
		}
		return true;
	}

	@Override
	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
		giveRewards(p);
	}

	@Override
	public ItemStack questItem(Player p) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + getName());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Requirements: ");
		for(String ci : amountToCollect.keySet()) {
			lore.add("Collect " + String.valueOf(amountToCollect.get(ci)) + " " + ci);
		}
		lore.add(ChatColor.GREEN + "\nCompleted: ");
		for(String ci : hasCollected.keySet()) {
			lore.add("Collected " + String.valueOf(hasCollected.get(ci)) + " " + ci);
		}
		if(isCompleted()) {
			lore.add("");
			lore.add(ChatColor.GOLD + "Ready For Turn In");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}

	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("MMOItemInformation");
		for(Object o : ja) {
			JSONObject entry = (JSONObject)o;
			String ci = entry.get("Item").toString();
			Integer amount = Integer.parseInt(entry.get("Amount").toString());
			addItemToCollect(ci, amount);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasCollected = new JSONArray();
		for(String ci : getHasCollected().keySet()) {
			JSONObject entry = new JSONObject();
			entry.put("Item", ci);
			entry.put("Amount", getHasCollected().get(ci));
			hasCollected.add(entry);
		}
		jo.put("MMOItems", hasCollected);
		return jo;
	}

	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if(jo.containsKey("MMOItems")) {
			JSONArray entries = (JSONArray)jo.get("MMOItems");
			for(Object o : entries) {
				JSONObject item = (JSONObject)o;
				String ci = item.get("Item").toString();
				Integer amount = Integer.parseInt(item.get("Amount").toString());
				getHasCollected().put(ci, amount);
			}
		}
	}
	
}