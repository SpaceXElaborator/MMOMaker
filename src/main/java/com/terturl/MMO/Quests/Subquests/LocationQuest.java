package com.terturl.MMO.Quests.Subquests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.Quests.Quest;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class LocationQuest extends Quest {

	@Getter @Setter
	private Location loc;
	
	public LocationQuest(String name) {
		super(name);
		setType(QuestType.LOCATION);
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
		LocationQuest q = new LocationQuest(getName());
		q.setLoc(getLoc());
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

	@Override
	public ItemStack questItem(Player p) {
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + getName());
		
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Requirements: ");
		lore.add(ChatColor.GREEN + "Move To: " + String.valueOf(getLoc().getX()) + " " + String.valueOf(getLoc().getY()) + " " + String.valueOf(getLoc().getZ()));
		im.setLore(lore);
		is.setItemMeta(im);
		
		return is;
	}
	
}