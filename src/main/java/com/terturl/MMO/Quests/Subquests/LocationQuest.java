package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.JSONHelpers.LocationUtils;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class LocationQuest extends Quest {

	@Getter @Setter
	private Location loc;

	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
		giveRewards(p);
	}

	public boolean hasComplete(Player p) {
		if(p.getLocation().distance(loc) <= 5.0) {
			return true;
		}
		return false;
	}

	@Override
	public Object clone() {
		LocationQuest q = new LocationQuest();
		q.setName(getName());
		q.setLoreForQuest(getLoreForQuest());
		q.setQuestType(getQuestType());
		q.setLoc(getLoc());
		q.setAcceptString(getAcceptString());
		q.setChildQuests(getChildQuests());
		q.setDenyString(getDenyString());
		q.setDescString(getDescString());
		q.setItems(getItems());
		q.setMoney(getMoney());
		q.setXp(getXp());
		q.setParentQuests(getParentQuests());
		q.setPresentString(getPresentString());
		return q;
	}

	@Override
	public List<String> requirementsLore() {
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Move To: " + String.valueOf(getLoc().getX()) + " " + String.valueOf(getLoc().getY()) + " " + String.valueOf(getLoc().getZ()));
		return lore;
	}

	@Override
	public JsonObject saveQuest() {
		return null;
	}

	@Override
	public void loadQuestToPlayer(JsonObject jo) {
		return;
	}

	@Override
	public void loadQuest(JsonObject jo) {
		setLoc(LocationUtils.locationDeSerializer(jo.get("Location").getAsString()));
	}
	
}