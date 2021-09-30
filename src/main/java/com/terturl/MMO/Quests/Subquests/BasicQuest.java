package com.terturl.MMO.Quests.Subquests;

import org.bukkit.entity.Player;

import com.terturl.MMO.Quests.Quest;

public class BasicQuest extends Quest {
	
	public BasicQuest(String name) {
		super(name);
	}

	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
	}

	public boolean hasComplete(Player p) {
		return false;
	}

	@Override
	public Object clone() {
		BasicQuest q = new BasicQuest(getName());
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