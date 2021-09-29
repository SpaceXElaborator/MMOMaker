package com.terturl.MMO.Quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Util.Items.CustomItem;

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
	private QuestType type;
	
	// Rewards
	@Getter @Setter
	private List<String> childQuests = new ArrayList<>();
	
	@Getter @Setter
	private Double money, xp = 0.0;
	
	@Getter @Setter
	private List<String> items = new ArrayList<>();
	
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
	
	public void giveRewards(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		if(money != 0) mc.setMoney(mc.getMoney() + money);
		if(xp != 0) mc.setXp(mc.getXp() + xp);
		if(childQuests.size() != 0) {
			for(String s : childQuests) {
				Quest q = MinecraftMMO.getInstance().getQuestManager().getQuest(s);
				mc.getActiveQuests().add(q);
			}
		}
		if(items.size() != 0) {
			for(String s : items) {
				CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(s);
				p.getInventory().addItem(ci.makeItem());
				p.updateInventory();
			}
		}
	}
	
	public abstract boolean hasComplete(Player p);
	public abstract void completeQuest(Player p);
	
}