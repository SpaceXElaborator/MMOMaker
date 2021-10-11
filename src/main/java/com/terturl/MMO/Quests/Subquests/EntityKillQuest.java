package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class EntityKillQuest extends Quest {

	@Getter @Setter
	public Map<EntityType, Integer> amountToKill = new HashMap<>();
	@Getter @Setter
	public Map<EntityType, Integer> hasKilled = new HashMap<>();
	
	public void addEntityToKill(EntityType et, Integer amount) {
		amountToKill.put(et, amount);
		hasKilled.put(et, 0);
	}
	
	@Override
	public boolean hasComplete(Player p) {
		for(EntityType et : amountToKill.keySet()) {
			if(amountToKill.get(et) != hasKilled.get(et)) return false;
		}
		return true;
	}

	@Override
	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
		giveRewards(p);
	}

	@Override
	public Object clone() {
		EntityKillQuest q = new EntityKillQuest();
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
		q.setAmountToKill(amountToKill);
		return q;
	}

	@Override
	public ItemStack questItem(Player p) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + getName());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Requirements: ");
		for(EntityType et : amountToKill.keySet()) {
			lore.add("Kill " + String.valueOf(amountToKill.get(et)) + " " + et.toString());
		}
		lore.add(ChatColor.GREEN + "\nCompleted: ");
		for(EntityType et : hasKilled.keySet()) {
			lore.add("Killed " + String.valueOf(hasKilled.get(et)) + " " + et.toString());
		}
		if(isCompleted()) {
			lore.add("");
			lore.add(ChatColor.GOLD + "Ready For Turn In");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasKilled = new JSONArray();
		for(EntityType et : getHasKilled().keySet()) {
			JSONObject entry = new JSONObject();
			entry.put("Type", et.toString());
			entry.put("Amount", getHasKilled().get(et));
			hasKilled.add(entry);
		}
		jo.put("Entities", hasKilled);
		return jo;
	}
	
	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if(jo.containsKey("Entities")) {
			JSONArray entries = (JSONArray)jo.get("Entities");
			for(Object o : entries) {
				JSONObject entity = (JSONObject)o;
				EntityType et = EntityType.valueOf(entity.get("Type").toString());
				Integer amount = Integer.parseInt(entity.get("Amount").toString());
				getHasKilled().put(et, amount);
			}
		}
	}

	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("EntityInformation");
		for(Object o : ja) {
			JSONObject entry = (JSONObject)o;
			EntityType et = EntityType.valueOf(entry.get("Type").toString().toUpperCase());
			Integer amount = Integer.parseInt(entry.get("Amount").toString());
			addEntityToKill(et, amount);
		}
	}

}