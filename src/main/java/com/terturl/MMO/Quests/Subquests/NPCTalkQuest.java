package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;

public class NPCTalkQuest extends Quest {

	@Getter @Setter
	private Map<String, List<String>> NPCList = new HashMap<>();
	
	@Getter @Setter
	private List<String> hasTalkedTo = new ArrayList<>();
	
	public NPCTalkQuest(String name) {
		super(name);
		setType(QuestType.TALKTONPC);
	}
	
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
		NPCTalkQuest q = new NPCTalkQuest(getName());
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
		q.setNPCList(getNPCList());
		q.setHasTalkedTo(getHasTalkedTo());
		return null;
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
	public ItemStack questItem(Player p) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + getName());
		
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Requirements: ");
		for(String s : NPCList.keySet()) {
			if(hasTalkedTo.contains(s)) continue;
			lore.add(ChatColor.GREEN + "Talk To: " + s);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
}