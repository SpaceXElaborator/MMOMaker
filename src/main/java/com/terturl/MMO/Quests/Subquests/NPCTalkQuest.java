package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;

public class NPCTalkQuest extends Quest {

	@Getter @Setter
	private Map<String, List<String>> NPCList = new HashMap<>();
	
	@Getter @Setter
	private List<String> hasTalkedTo = new ArrayList<>();
	
	public void addNPC(String s) {
		if(NPCList.containsKey(s)) return;
		NPCList.put(s, new ArrayList<>());
	}
	
	public boolean containsNPC(String s) {
		return NPCList.containsKey(s);
	}
	
	public void addDialog(String name, String s) {
		if(!NPCList.containsKey(name)) return;
		List<String> dialog = NPCList.get(name);
		dialog.add(s);
		NPCList.put(name, dialog);
	}
	
	public void talkTo(String s, Player p) {
		if(hasTalkedTo.contains(s)) return;
		for(String dialog : NPCList.get(s)) {
			p.sendMessage(s + " " + ChatColor.GREEN + dialog);
		}
		hasTalkedTo.add(s);
	}
	
	@Override
	public Object clone() {
		NPCTalkQuest q = new NPCTalkQuest();
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
		q.setNPCList(getNPCList());
		q.setHasTalkedTo(getHasTalkedTo());
		return q;
	}
	
	@Override
	public boolean hasComplete(Player p) {
		for(String s : NPCList.keySet()) {
			if(!hasTalkedTo.contains(s)) return false;
		}
		return true;
	}
	
	@Override
	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
		giveRewards(p);
	}
	
	@Override
	public List<String> requirementsLore() {
		List<String> lore = new ArrayList<>();
		for(String s : NPCList.keySet()) {
			if(hasTalkedTo.contains(s)) {
				lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.GREEN + "\u2714 Talked To " + s);
			} else {
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + "\u2715 " + ChatColor.RED + "Talk To " + s);
			}
		}
		return lore;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasTalkedTo = new JSONArray();
		for(String talkedTo : getHasTalkedTo()) {
			hasTalkedTo.add(talkedTo);
		}
		jo.put("TalkedTo", hasTalkedTo);
		return jo;
	}

	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if(jo.containsKey("TalkedTo")) {
			JSONArray talkedTo = (JSONArray)jo.get("TalkedTo");
			for(Object o : talkedTo) {
				String s = o.toString();
				getHasTalkedTo().add(s);
			}
		}
	}

	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray dialog = (JSONArray) jo.get("NPCS");
		for(Object o : dialog) {
			JSONObject npc = (JSONObject)o;
			String npcName = npc.get("Name").toString();
			addNPC(npcName);
			if(npc.containsKey("Dialog")) {
				JSONArray dialogStrings = (JSONArray)npc.get("Dialog");
				for(Object dialogString : dialogStrings) {
					String s = dialogString.toString();
					addDialog(npcName, s);
				}
			}
		}
	}
	
}