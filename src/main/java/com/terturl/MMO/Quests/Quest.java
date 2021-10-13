package com.terturl.MMO.Quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Subquests.EntityKillQuest;
import com.terturl.MMO.Quests.Subquests.LocationQuest;
import com.terturl.MMO.Quests.Subquests.NPCTalkQuest;
import com.terturl.MMO.Util.Items.CustomItem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

@EqualsAndHashCode
public abstract class Quest {
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String descString, acceptString, DenyString, presentString;
	
	@Getter @Setter
	private String questType;
	
	@Getter @Setter
	private boolean requireTurnIn = true;
	
	@Getter @Setter
	private boolean completed = false;
	
	@Getter @Setter
	private List<String> loreForQuest = new ArrayList<>();
	
	@Getter @Setter
	private List<String> parentQuests = new ArrayList<>();
	
	// Rewards
	@Getter @Setter
	private List<String> childQuests = new ArrayList<>();
	
	@Getter @Setter
	private Double money, xp = 0.0;
	
	@Getter @Setter
	private List<String> items = new ArrayList<>();
	
	@Getter @Setter
	private List<String> abilities = new ArrayList<>();
	
	@Getter @Setter
	private List<String> recipes = new ArrayList<>();
	
	public Quest() {}
	
	public abstract Object clone();
	
	public void giveRewards(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		if(money > 0) mc.addMoney(money);
		if(xp > 0) mc.addXP(xp);
		if(childQuests.size() > 0) {
			for(String s : childQuests) {
				Object q = null;
				q = MinecraftMMO.getInstance().getQuestManager().getQuest(s);
				
				if(q instanceof LocationQuest) {
					q = (LocationQuest)q;
				} else if(q instanceof EntityKillQuest) {
					q = (EntityKillQuest)q;
				} else if(q instanceof NPCTalkQuest) {
					q = (NPCTalkQuest)q;
				}
				
				mc.getActiveQuests().add((Quest) q);
			}
		}
		if(items.size() > 0) {
			for(String s : items) {
				CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(s);
				p.getInventory().addItem(ci.makeItem());
				p.updateInventory();
			}
		}
		if(recipes.size() > 0) {
			for(String s : recipes) {
				if(mc.getCraftingRecipes().contains(s)) continue;
				mc.getCraftingRecipes().add(s);
			}
		}
		if(abilities.size() > 0) {
			for(String s : abilities) {
				if(mc.getPlayerAbilities().contains(s)) continue;
				mc.getPlayerAbilities().add(s);
			}
		}
	}
	
	public void finishQuest(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		if(requireTurnIn) {
			if(mc.getCompletedableQuests().contains(this)) return;
			p.sendMessage("Completed Quest, go turn in");
			mc.getCompletedableQuests().add(this);
			completed = true;
		} else {
			p.sendMessage("Completed Quest");
			completeQuest(p);
			mc.getCompletedQuests().add(getName());
			mc.getActiveQuests().remove(this);
		}
		mp.updateNPCQuests();
	}
	
	protected List<String> getRewardsLore() {
		List<String> lore = new ArrayList<>();
		if(getXp() > 0 || getMoney() > 0 || getRecipes().size() > 0 || getItems().size() > 0) {
			lore.add("");
			lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Rewards:");
			if(getXp() > 0) lore.add(ChatColor.GRAY + "\u2192 XP: " + String.valueOf(getXp()));
			if(getMoney() > 0) lore.add(ChatColor.GRAY + "\u2192 Money: " + String.valueOf(getMoney()));
			if(getRecipes().size() > 0) {
				for(String s : getRecipes()) {
					lore.add(ChatColor.GRAY + "\u2192 Recipe: " + s);
				}
			}
			if(getItems().size() > 0) {
				for(String s : getItems()) {
					lore.add(ChatColor.GRAY + "\u2192 Item: " + s);
				}
			}
		}
		return lore;
	}
	
	public ItemStack questItem(Player p) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + getName());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Requirements: ");
		lore.addAll(requirementsLore());
		lore.addAll(getRewardsLore());
		if(getLoreForQuest().size() > 0) {
			lore.add("");
			for(String s : getLoreForQuest()) {
				lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + s);
			}
		}
		if(isCompleted()) {
			lore.add("");
			lore.add(ChatColor.GOLD + "Ready For Turn In");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public abstract boolean hasComplete(Player p);
	public abstract void completeQuest(Player p);
	public abstract List<String> requirementsLore();
	
	public abstract void loadQuest(JSONObject jo);
	public abstract JSONObject saveQuest();
	public abstract void loadQuestToPlayer(JSONObject jo);
	
}