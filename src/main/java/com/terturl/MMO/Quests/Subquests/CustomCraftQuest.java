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
 * CustomCraftQuest holds all the CustomItems the player must craft. Will also
 * create, load, and save information to the players save file
 * 
 * @author Sean Rahman
 * @since 0.43.0
 *
 */
public class CustomCraftQuest extends Quest {

	@Getter
	@Setter
	private Map<String, Integer> toCraft = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Integer> hasCrafted = new HashMap<>();

	@Override
	public Object clone() {
		CustomCraftQuest q = new CustomCraftQuest();
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
		q.setToCraft(toCraft);
		q.setHasCrafted(hasCrafted);
		return q;
	}

	/**
	 * Adds an item by string to the craft list
	 * @param s Name of CustomItem to craft
	 * @param a Amount the player has to craft
	 */
	public void addItemToCraft(String s, Integer a) {
		toCraft.put(s, a);
		hasCrafted.put(s, 0);
	}

	/**
	 * @see Quest#hasComplete(Player)
	 */
	@Override
	public boolean hasComplete(Player p) {
		for (String s : toCraft.keySet()) {
			if (toCraft.get(s) != hasCrafted.get(s))
				return false;
		}
		return true;
	}

	/**
	 * @see Quest#completeQuest(Player)
	 */
	@Override
	public void completeQuest(Player p) {
		giveRewards(p);
		p.sendMessage("Completed quest");
	}

	/**
	 * @see Quest#requirementsLore()
	 */
	@Override
	public List<String> requirementsLore() {
		List<String> lore = new ArrayList<>();
		for (String s : toCraft.keySet()) {
			if (toCraft.get(s) <= hasCrafted.get(s)) {
				lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.GREEN + "\u2714 Crafted " + s);
			} else {
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + "\u2715 " + ChatColor.RED
						+ String.valueOf(hasCrafted.get(s) + "/" + String.valueOf(toCraft.get(s)) + s + " Crafted"));
			}
		}
		return lore;
	}

	/**
	 * @see Quest#loadQuest(JSONObject)
	 */
	@Override
	public void loadQuest(JsonObject jo) {
		if(!jo.has("CraftingInformation") || !jo.get("CraftingInformation").isJsonArray()) return;
		
		JsonArray ja = jo.get("CraftingInformation").getAsJsonArray();
		for (JsonElement o : ja) {
			if(!o.isJsonObject()) continue;
			JsonObject entry = o.getAsJsonObject();
			String s = entry.get("Item").getAsString();
			int amount = entry.get("Amount").getAsInt();
			addItemToCraft(s, amount);
		}
	}

	/**
	 * @see Quest#saveQuest()
	 */
	@Override
	public JsonObject saveQuest() {
		JsonObject jo = new JsonObject();
		JsonArray hasCrafted = new JsonArray();
		for (String s : getHasCrafted().keySet()) {
			JsonObject entry = new JsonObject();
			entry.addProperty("Item", s);
			entry.addProperty("Amount", getHasCrafted().get(s));
			hasCrafted.add(entry);
		}
		jo.add("Crafted", hasCrafted);
		return jo;
	}

	/**
	 * @see Quest#loadQuestToPlayer(JSONObject)
	 */
	@Override
	public void loadQuestToPlayer(JsonObject jo) {
		if(!jo.has("Crafted") || !jo.get("Crafted").isJsonArray()) return;
		
		JsonArray entries = jo.get("Crafted").getAsJsonArray();
		for (JsonElement o : entries) {
			if(!o.isJsonObject()) continue;
			JsonObject entity = o.getAsJsonObject();
			String et = entity.get("Item").getAsString();
			int amount = entity.get("Amount").getAsInt();
			getHasCrafted().put(et, amount);
		}
	}

}