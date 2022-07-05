package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
	public void loadQuest(JsonObject jo) {
		if(!jo.has("MMOItemInformation") || !jo.get("MMOItemInformation").isJsonArray()) return;
		
		JsonArray ja = jo.get("MMOItemInformation").getAsJsonArray();
		for (JsonElement o : ja) {
			if(!o.isJsonObject()) continue;
			JsonObject entry = o.getAsJsonObject();
			String ci = entry.get("Item").getAsString();
			int amount = entry.get("Amount").getAsInt();
			addItemToCollect(ci, amount);
		}
	}

	/**
	 * @see Quest#saveQuest()
	 */
	@Override
	public JsonObject saveQuest() {
		JsonObject jo = new JsonObject();
		JsonArray hasCollected = new JsonArray();
		for (String ci : getHasCollected().keySet()) {
			JsonObject entry = new JsonObject();
			entry.addProperty("Item", ci);
			entry.addProperty("Amount", getHasCollected().get(ci));
			hasCollected.add(entry);
		}
		jo.add("MMOItems", hasCollected);
		return jo;
	}

	/**
	 * @see Quest#loadQuestToPlayer(JSONObject)
	 */
	@Override
	public void loadQuestToPlayer(JsonObject jo) {
		if(!jo.has("MMOItems") || !jo.get("MMOItems").isJsonObject()) return;
		
		JsonArray entries = jo.get("MMOItems").getAsJsonArray();
		for (JsonElement o : entries) {
			if(!o.isJsonObject()) continue;
			JsonObject item = o.getAsJsonObject();
			String ci = item.get("Item").getAsString();
			int amount = item.get("Amount").getAsInt();
			getHasCollected().put(ci, amount);
		}
	}

}