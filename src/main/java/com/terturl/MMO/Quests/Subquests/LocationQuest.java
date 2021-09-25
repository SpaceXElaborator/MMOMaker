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
	}

	public boolean hasComplete(Player p) {
		if(p.getLocation().distance(loc) <= 5.0) {
			return true;
		}
		return false;
	}
	
}