package com.terturl.MMO.Quests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Quests.Subquests.BasicQuest;
import com.terturl.MMO.Quests.Subquests.LocationQuest;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.LocationUtils;

import lombok.Getter;

public class QuestManager {

	@Getter
	private List<Quest> allQuests = new ArrayList<>();
	
	public QuestManager() {
		File questDir = new File(MinecraftMMO.getInstance().getDataFolder(), "quests");
		if(!questDir.exists()) questDir.mkdir();
		
		for(File f : questDir.listFiles()) {
			if(f.getName().endsWith(".json")) {
				JsonFileInterpretter config = new JsonFileInterpretter(f);
				String name = config.contains("Name") ? config.getString("Name") : null;
				String descString = config.contains("Description") ? config.getString("Description") : null;
				String presentString = config.contains("Present") ? config.getString("Present") : "Would you like to ACCEPT or DENY?";
				String denyString = config.contains("Deny") ? config.getString("Deny") : "That's Very Sad Traveller";
				String acceptString = config.contains("Accept") ? config.getString("Accept") : "Thank you Traveller!";
				String type = config.contains("Type") ? config.getString("Type") : "Basic";
				List<String> parentQuests = config.contains("Parents") ? config.getStringList("Parents") : new ArrayList<>();
				if(name == null || descString == null) continue;
				
				if(type.equalsIgnoreCase("location")) {
					LocationQuest lq = new LocationQuest(name, LocationUtils.locationDeSerializer(config.getString("Location")));
					lq.setDescString(descString);
					lq.setAcceptString(acceptString);
					lq.setDenyString(denyString);
					lq.setPresentString(presentString);
					lq.setParentQuests(parentQuests);
					allQuests.add(lq);
				} else {
					Quest q = new BasicQuest(name);
					q.setDescString(descString);
					q.setAcceptString(acceptString);
					q.setDenyString(denyString);
					q.setPresentString(presentString);
					q.setParentQuests(parentQuests);
					allQuests.add(q);
				}
			}
		}
	}
	
	public Quest getQuest(String s) {
		return allQuests.stream().filter(e -> e.getName().equals(s)).findFirst().orElse(null);
	}
	
}