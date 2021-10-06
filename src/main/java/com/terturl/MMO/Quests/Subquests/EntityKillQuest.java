package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

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
		hasKilled.put(et, 0);
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

	@Override
	public Object clone() {
		EntityKillQuest q = new EntityKillQuest(getName());
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
		q.setAmountToKill(amountToKill);
		q.setHasKilled(hasKilled);
		return q;
	}

	@Override
	public ItemStack questItem(Player p) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + getName());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Requirements: ");
		for(EntityType et : amountToKill.keySet()) {
			lore.add("Kill " + String.valueOf(amountToKill.get(et)) + et.toString());
		}
		lore.add(ChatColor.GREEN + "\nCompleted: ");
		for(EntityType et : hasKilled.keySet()) {
			lore.add("Killed " + String.valueOf(hasKilled.get(et)) + et.toString());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}

}