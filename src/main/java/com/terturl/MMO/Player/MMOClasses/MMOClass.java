package com.terturl.MMO.Player.MMOClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Handles the level, quests, abilities, recipes, and skills of a players
 * selected class
 * 
 * @author Sean Rahman
 *
 */
@EqualsAndHashCode
public class MMOClass implements Cloneable {

	@Getter
	@Setter
	private int level = 1;

	@Getter @Setter
	private Double Mana;
	
	@Getter @Setter
	private Double MaxHealth;
	
	@Getter @Setter
	private Double Health;
	
	@Getter @Setter
	private Double damage = 0.0d;
	
	@Getter @Setter
	private Double armor = 0.0d;
	
	@Getter
	private List<String> completedQuests = new ArrayList<>();
	@Getter
	private List<Quest> activeQuests = new ArrayList<>();
	@Getter
	private List<String> completedableQuests = new ArrayList<>();

	@Getter
	private CraftingSkill craftSkill = new CraftingSkill();

	@Getter
	private List<String> craftingRecipes = new ArrayList<>();

	@Getter
	private List<String> playerAbilities = new ArrayList<>();

	@Getter
	private List<Skill> playerSkills = new ArrayList<>();

	@Getter
	@Setter
	private Ability hotAbility1, hotAbility2, hotAbility3;

	@Getter
	@Setter
	private Double money = 0.0;
	@Getter
	@Setter
	private Double xp = 0.0;

	@Getter
	@Setter
	private Location classLocation;

	@Getter
	private String name;

	@Getter
	@Setter
	private Map<SlotType, String> startItems = new HashMap<>();

	@Getter
	@Setter
	private String helmet, chest, legs, boots, mainH, offH;

	/**
	 * Creates a new MMOClass with the given name
	 * 
	 * @param name Name of new MMOClass
	 */
	public MMOClass(String name) {
		this.name = name;
	}

	/**
	 * Checks if a Quest is within the players active quest list
	 * 
	 * @param q Quest to check for
	 * @return Quests present or not
	 */
	public boolean hasActiveQuest(String q) {
		for (Quest quest : activeQuests) {
			if (quest.getName().equalsIgnoreCase(q))
				return true;
		}
		return false;
	}

	/**
	 * Checks if a Quest is within the players completedable quest list
	 * 
	 * @param q Quest to check for
	 * @return Quest is present or not
	 */
	public boolean hasCompletableQuest(String q) {
		return completedableQuests.contains(q);
	}

	/**
	 * Removes a quest from the completedable quest list
	 * 
	 * @param q Quest to remove
	 */
	public void removeCompletableQuest(String q) {
		completedableQuests.remove(q);
	}

	/**
	 * Remove's the Quest with the given name from the MMOClass's active Quest list
	 * 
	 * @param q Name of quest
	 */
	public void removeActiveQuest(String q) {
		int toRemove = -1;
		for (int i = 0; i < activeQuests.size(); i++) {
			if (activeQuests.get(i).getName().equalsIgnoreCase(q)) {
				toRemove = i;
				break;
			}
		}

		if (toRemove != -1) {
			activeQuests.remove(toRemove);
		}
	}

	/**
	 * Adds a Quest to the players Active Quest list
	 * 
	 * @param q Quest to add
	 */
	public void addQuest(Quest q) {
		getActiveQuests().add(q);
	}

	/**
	 * Checks if the player has completed a quest
	 * 
	 * @param q Quest to check for
	 * @return If the player has completed the Quest or not
	 */
	public boolean hasCompletedQuest(String q) {
		return completedQuests.contains(q);
	}

	/**
	 * Checks of the player has completed all parent quests for a specified quest
	 * 
	 * @param q Quest to check parents of
	 * @return If player has completed parent quests
	 */
	public boolean hasParentQuestsCompleted(String q) {
		Quest quest = MinecraftMMO.getInstance().getQuestManager().getQuest(q);
		for (String pQuests : quest.getParentQuests()) {
			if (!completedQuests.contains(pQuests))
				return false;
		}
		return true;
	}

	/**
	 * Add the specified money to the players currency
	 * 
	 * @param b Double to add to Money
	 */
	public void addMoney(Double b) {
		money += b;
	}

	/**
	 * Adds the specified amount of XP to the player and levels them up if need be
	 * 
	 * @param d Double to add to XP
	 */
	public void addXP(Double d) {
		Double finalXP = getNextLevelXP();
		if (xp + d >= finalXP) {
			Double rollOver = (xp + d) - finalXP;
			setLevel(getLevel() + 1);
			addXP(rollOver);
		}
		xp += d;
	}

	/**
	 * Checks for how much XP is needed to level up based on the math configuration
	 * string
	 * 
	 * @return Amount of XP needed in Double
	 */
	public double getNextLevelXP() {
		Argument CL = new Argument("CL = " + String.valueOf(getLevel()));
		Argument NL = new Argument("NL = " + String.valueOf(getLevel() + 1));
		Argument XP = new Argument("XP = " + String.valueOf(getXp()));
		Expression e = new Expression(MinecraftMMO.getInstance().getMathConfig().getPlayerLevelEquation(), CL, NL, XP);
		Double finalXP = e.calculate();
		return finalXP;
	}

	/**
	 * Updates the players inventory to show the new stats on the item in the upper
	 * left of their inventory
	 * 
	 * @param p Player to get inventory of
	 */
	public void updateClassInformation(Player p) {
		ItemStack is = BasicInventoryItems.getPlayerClassItem(p);
		p.getInventory().setItem(9, is);
		p.updateInventory();
	}

	/**
	 * Checks if the MMOClass contains a specific skill or not
	 * 
	 * @param s Name of Skill to check for
	 * @return If the MMOClass contains a skill or not
	 */
	public boolean containsSkill(String s) {
		for (Skill skill : playerSkills) {
			if (skill.getSkillName().equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	 * Adds the specified XP to the specified skill
	 * 
	 * @param s  Name of Skill
	 * @param xp XP to add to skill
	 */
	public void addXpToSkill(String s, Double xp) {
		if (!containsSkill(s))
			return;
		playerSkills.forEach(e -> {
			if (e.getSkillName().equalsIgnoreCase(s)) {
				e.addXP(xp);
			}
		});
	}

	/**
	 * Get all quests with the specified type
	 * 
	 * @param s Type of Quest
	 * @return
	 */
	public List<Quest> getQuestsWithType(String s) {
		return getActiveQuests().stream().filter(q -> q.getQuestType().equals(s)).collect(Collectors.toList());
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