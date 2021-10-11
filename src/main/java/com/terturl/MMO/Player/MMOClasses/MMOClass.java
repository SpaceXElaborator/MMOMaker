package com.terturl.MMO.Player.MMOClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Abilities.Ability;
import com.terturl.MMO.Player.Skills.Skill;
import com.terturl.MMO.Player.Skills.Crafting.CraftingSkill;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.BasicInventoryItems;
import com.terturl.MMO.Util.Items.CustomItem.SlotType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class MMOClass implements Cloneable {

	@Getter
	@Setter
	private int level = 1;

	@Getter
	private List<String> completedQuests = new ArrayList<>();
	@Getter
	private List<Quest> activeQuests = new ArrayList<>();
	@Getter
	private List<Quest> completedableQuests = new ArrayList<>();
	
	@Getter
	private CraftingSkill craftSkill = new CraftingSkill();
	
	@Getter
	private List<String> craftingRecipes = new ArrayList<>();
	
	@Getter
	private List<String> playerAbilities = new ArrayList<>();
	
	@Getter
	private List<Skill> playerSkills = new ArrayList<>();
	
	@Getter @Setter
	private Ability hotAbility1, hotAbility2, hotAbility3;
	
	@Getter @Setter
	private Double money = 0.0;
	@Getter @Setter
	private Double xp = 0.0;
	
	@Getter @Setter
	private Location classLocation;
	
	@Getter
	private String name;
	
	@Getter @Setter
	private Map<SlotType, String> startItems = new HashMap<>();
	
	@Getter @Setter
	private String helmet, chest, legs, boots, mainH, offH;
	
	public MMOClass(String name) {
		this.name = name;
	}
	
	public boolean hasActiveQuest(Quest q) {
		return activeQuests.contains(q);
	}
	
	public boolean hasCompletableQuest(Quest q) {
		return completedableQuests.contains(q);
	}
	
	public void removeCompletableQuest(Quest q) {
		int toRemove = -1;
		for(int i = 0; i < completedableQuests.size(); i++) {
			if(q.getName().equalsIgnoreCase(completedableQuests.get(i).getName())) {
				toRemove = i;
				break;
			}
		}
		
		if(toRemove != -1) {
			completedableQuests.remove(toRemove);
		}
	}

	public boolean hasCompletedQuest(Quest q) {
		return completedQuests.contains(q.getName());
	}
	
	public boolean hasParentQuestsCompleted(Quest q) {
		for(String quest : q.getParentQuests()) {
			if(!completedQuests.contains(quest)) return false;
		}
		return true;
	}
	
	public void addMoney(Double b) {
		money += b;
	}
	
	public void addXP(Double d) {
		Double finalXP = getNextLevelXP();
		if(xp+d >= finalXP) {
			Double rollOver = (xp+d) - finalXP;
			setLevel(getLevel() + 1);
			addXP(rollOver);
		}
		xp += d;
	}
	
	public double getNextLevelXP() {
		Argument CL = new Argument("CL = " + String.valueOf(getLevel()));
		Argument NL = new Argument("NL = " + String.valueOf(getLevel() + 1));
		Argument XP = new Argument("XP = " + String.valueOf(getXp()));
		Expression e = new Expression(MinecraftMMO.getInstance().getMathConfig().getPlayerLevelEquation(), CL, NL, XP);
		Double finalXP = e.calculate();
		return finalXP;
	}
	
	public void updateClassInformation(Player p) {
		ItemStack is = BasicInventoryItems.getPlayerClassItem(p);
		p.getInventory().setItem(9, is);
		p.updateInventory();
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}