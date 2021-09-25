package com.terturl.MMO.Quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public abstract class Quest implements Cloneable {
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String descString, acceptString, DenyString, presentString;
	
	@Getter @Setter
	private List<String> parentQuests = new ArrayList<>();
	
	@Getter @Setter
	private Quest childQuest = null;
	
	@Getter @Setter
	private QuestType type;
	
	public Quest(String name) {
		setName(name);
	}
	
	public enum QuestType {
		LOCATION;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract boolean hasComplete(Player p);
	public abstract void completeQuest(Player p);
	
}