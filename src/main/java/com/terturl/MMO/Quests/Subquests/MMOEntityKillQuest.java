package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

/**
 * MMOEntityKillQuest focuses on the killing of specifically named entities created from the Entity package
 * @see com.terturl.MMO.Entity.MMOEntity
 * @author Sean Rahman
 * @since 0.52.0
 *
 */
public class MMOEntityKillQuest extends Quest {

	@Getter @Setter
	public Map<String, Integer> amountToKill = new HashMap<>();
	@Getter @Setter
	public Map<String, Integer> hasKilled = new HashMap<>();
	
	public void addEntityToKill(String et, Integer amount) {
		amountToKill.put(et, amount);
		hasKilled.put(et, 0);
	}
	
	/**
	 * @see Quest#hasComplete(Player)
	 */
	@Override
	public boolean hasComplete(Player p) {
		for(String et : amountToKill.keySet()) {
			if(amountToKill.get(et) != hasKilled.get(et)) return false;
		}
		return true;
	}

	/**
	 * @see Quest#completeQuest(Player)
	 */
	@Override
	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
		giveRewards(p);
	}

	@Override
	public Object clone() {
		MMOEntityKillQuest q = new MMOEntityKillQuest();
		q.setName(getName());
		q.setLoreForQuest(getLoreForQuest());
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

	/**
	 * @see Quest#requirementsLore()
	 */
	@Override
	public List<String> requirementsLore() {
		List<String> lore = new ArrayList<>();
		for(String s : amountToKill.keySet()) {
			if(amountToKill.get(s) <= hasKilled.get(s)) {
				lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.GREEN + "\u2714 Killed " + s);
			} else {
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + "\u2715 " + ChatColor.RED + String.valueOf(hasKilled.get(s) + "/" + String.valueOf(amountToKill.get(s)) + " " + s + " Killed"));
			}
		}
		return lore;
	}

	/**
	 * @see Quest#saveQuest()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasKilled = new JSONArray();
		for(String et : getHasKilled().keySet()) {
			JSONObject entry = new JSONObject();
			entry.put("MMOEntity", et);
			entry.put("Amount", getHasKilled().get(et));
			hasKilled.add(entry);
		}
		jo.put("Entities", hasKilled);
		return jo;
	}
	
	/**
	 * @see Quest#loadQuestToPlayer(JSONObject)
	 */
	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if(jo.containsKey("Entities")) {
			JSONArray entries = (JSONArray)jo.get("Entities");
			for(Object o : entries) {
				JSONObject entity = (JSONObject)o;
				String et = entity.get("MMOEntity").toString();
				Integer amount = Integer.parseInt(entity.get("Amount").toString());
				getHasKilled().put(et, amount);
			}
		}
	}

	/**
	 * @see Quest#loadQuest(JSONObject)
	 */
	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("EntityInformation");
		for(Object o : ja) {
			JSONObject entry = (JSONObject)o;
			String et = entry.get("MMOEntity").toString();
			Integer amount = Integer.parseInt(entry.get("Amount").toString());
			addEntityToKill(et, amount);
		}
	}
	
}