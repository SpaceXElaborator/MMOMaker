package com.terturl.MMO.Player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.Abilities.Ability;
import com.terturl.MMO.Player.Skills.Skill;
import com.terturl.MMO.Player.Skills.Crafting.CraftingSkill;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.BasicInventoryItems;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public abstract class MMOClass implements Cloneable {

	@Getter
	@Setter
	private int Level = 1;

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
	private ItemStack helmet, chest, legs, boots, mainH, offH;
	
	public MMOClass(String name) {
		this.name = name;
	}

	public abstract ItemStack startMainHand();
	
	public abstract ItemStack startOffHand();
	
	public abstract ItemStack starterHelmet();
	
	public abstract ItemStack starterChestplate();
	
	public abstract ItemStack starterLeggings();
	
	public abstract ItemStack starterBoots();
	
	public boolean hasActiveQuest(Quest q) {
		Quest quest = activeQuests.stream().filter(e -> e.equals(q)).findFirst().orElse(null);
		if(quest != null) return true;
		return false;
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
	
	// TODO: Make level progression
	public void addXP(Double b) {
		xp += b;
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