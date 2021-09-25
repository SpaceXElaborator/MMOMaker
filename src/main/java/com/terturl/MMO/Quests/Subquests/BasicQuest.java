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
	
}