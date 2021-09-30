package com.terturl.MMO.Quests.Subquests;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;

public class LocationQuest extends Quest {

	@Getter @Setter
	private Location loc;
	
	public LocationQuest(String name, Location location) {
		super(name);
		setType(QuestType.LOCATION);
		loc = location;
	}

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
		LocationQuest q = new LocationQuest(getName(), getLoc());
		q.setAcceptString(getAcceptString());
		q.setChildQuests(getChildQuests());
		q.setDenyString(getDenyString());
		q.setDescString(getDescString());
		q.setItems(getItems());
		q.setMoney(getMoney());
		q.setXp(getXp());
		q.setParentQuests(getParentQuests());
		q.setPresentString(getPresentString());
		q.setType(getType());
		return q;
	}
	
}