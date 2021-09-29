package com.terturl.MMO.Quests.Subquests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;

public class EntityKillQuest extends Quest {

	@Getter @Setter
	public Map<EntityType, Integer> amountToKill = new HashMap<>();
	@Getter @Setter
	public Map<EntityType, Integer> hasKilled = new HashMap<>();
	
	public EntityKillQuest(String name) {
		super(name);
		setType(QuestType.KILLENTITY);
	}
	
	public void addEntityToKill(EntityType et, Integer amount) {
		amountToKill.put(et, amount);
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

}