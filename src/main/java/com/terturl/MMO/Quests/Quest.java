package com.terturl.MMO.Quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

@EqualsAndHashCode
public abstract class Quest {
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String descString, acceptString, DenyString, presentString;
	
	@Getter @Setter
	private String questType;
	
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
		if(money != 0) mc.addMoney(money);
		if(xp != 0) mc.addXP(xp);
		if(childQuests.size() != 0) {
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
		if(items.size() != 0) {
			for(String s : items) {
				CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(s);
				p.getInventory().addItem(ci.makeItem());
				p.updateInventory();
			}
		}
		if(recipes.size() != 0) {
			for(String s : recipes) {
				if(mc.getCraftingRecipes().contains(s)) continue;
				mc.getCraftingRecipes().add(s);
			}
		}
	}
	
	public abstract boolean hasComplete(Player p);
	public abstract void completeQuest(Player p);
	public abstract ItemStack questItem(Player p);
	
	public abstract void loadQuest(JSONObject jo);
	public abstract JSONObject saveQuest();
	public abstract void loadQuestToPlayer(JSONObject jo);
	
}