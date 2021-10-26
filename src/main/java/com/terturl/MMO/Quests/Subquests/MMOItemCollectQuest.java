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
 * Handles the saving, loading, and creation of any CustomItem collecting quest
 * 
 * @author Sean Rahman
 * @since 0.48.0
 */
public class MMOItemCollectQuest extends Quest {

	@Getter
	@Setter
	public Map<String, Integer> amountToCollect = new HashMap<>();
	@Getter
	@Setter
	public Map<String, Integer> hasCollected = new HashMap<>();

	public void addItemToCollect(String ci, Integer amount) {
		amountToCollect.put(ci, amount);
		hasCollected.put(ci, 0);
	}

	@Override
	public Object clone() {
		MMOItemCollectQuest q = new MMOItemCollectQuest();
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
		q.setAmountToCollect(getAmountToCollect());
		return q;
	}

	/**
	 * @see Quest#hasComplete(Player)
	 */
	@Override
	public boolean hasComplete(Player p) {
		for (String ci : amountToCollect.keySet()) {
			if (amountToCollect.get(ci) != hasCollected.get(ci))
				return false;
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

	/**
	 * @see Quest#requirementsLore()
	 */
	@Override
	public List<String> requirementsLore() {
		List<String> lore = new ArrayList<>();
		for (String s : amountToCollect.keySet()) {
			if (amountToCollect.get(s) <= hasCollected.get(s)) {
				lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.GREEN + "\u2714 Collected " + s);
			} else {
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + "\u2715 " + ChatColor.RED + String.valueOf(
						hasCollected.get(s) + "/" + String.valueOf(amountToCollect.get(s)) + s + " Collected"));
			}
		}
		return lore;
	}

	/**
	 * @see Quest#loadQuest(JSONObject)
	 */
	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("MMOItemInformation");
		for (Object o : ja) {
			JSONObject entry = (JSONObject) o;
			String ci = entry.get("Item").toString();
			Integer amount = Integer.parseInt(entry.get("Amount").toString());
			addItemToCollect(ci, amount);
		}
	}

	/**
	 * @see Quest#saveQuest()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasCollected = new JSONArray();
		for (String ci : getHasCollected().keySet()) {
			JSONObject entry = new JSONObject();
			entry.put("Item", ci);
			entry.put("Amount", getHasCollected().get(ci));
			hasCollected.add(entry);
		}
		jo.put("MMOItems", hasCollected);
		return jo;
	}

	/**
	 * @see Quest#loadQuestToPlayer(JSONObject)
	 */
	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if (jo.containsKey("MMOItems")) {
			JSONArray entries = (JSONArray) jo.get("MMOItems");
			for (Object o : entries) {
				JSONObject item = (JSONObject) o;
				String ci = item.get("Item").toString();
				Integer amount = Integer.parseInt(item.get("Amount").toString());
				getHasCollected().put(ci, amount);
			}
		}
	}

}