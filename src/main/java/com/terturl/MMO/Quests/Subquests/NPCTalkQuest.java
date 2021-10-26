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

/**
 * NPCTalkQuest holds all the NPCs the player must talk to as well as if the
 * player has talked to the entity or not. Will also create, load, and save
 * information to the players save file
 * 
 * @author Sean Rahman
 * @since 0.40.0
 *
 */
public class NPCTalkQuest extends Quest {

	@Getter
	@Setter
	private Map<String, List<String>> NPCList = new HashMap<>();

	@Getter
	@Setter
	private List<String> hasTalkedTo = new ArrayList<>();

	/**
	 * Adds an NPC by name to the list to talk to. If the NPC does not exit, this
	 * function will fail
	 * 
	 * @param s The Name of the NPC
	 */
	public void addNPC(String s) {
		if (NPCList.containsKey(s))
			return;
		NPCList.put(s, new ArrayList<>());
	}

	/**
	 * Method to check if the NPC's name is in the NPCList
	 * 
	 * @param s The name of the NPC
	 * @return If the NPCList contains the NPC's name
	 */
	public boolean containsNPC(String s) {
		return NPCList.containsKey(s);
	}

	/**
	 * Adds dialog when talking to an NPC
	 * These will all be called when the player talks to the NPC to be able to add some customization and immersion
	 * @param name Name of the NPC
	 * @param s The dialog string to add to the NPC
	 */
	public void addDialog(String name, String s) {
		if (!NPCList.containsKey(name))
			return;
		List<String> dialog = NPCList.get(name);
		dialog.add(s);
		NPCList.put(name, dialog);
	}

	/**
	 * Handles when an NPC "talks" to a player
	 * @param s The NPC to get dialog from
	 * @param p The player that "talked" to the NPC
	 */
	public void talkTo(String s, Player p) {
		if (hasTalkedTo.contains(s))
			return;
		for (String dialog : NPCList.get(s)) {
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

	/**
	 * @see Quest#hasComplete(Player)
	 */
	@Override
	public boolean hasComplete(Player p) {
		for (String s : NPCList.keySet()) {
			if (!hasTalkedTo.contains(s))
				return false;
		}
		return true;
	}

	/**
	 * @see Quest#completeQuest(Player)
	 */
	@Override
	public void completeQuest(Player p) {
		p.sendMessage("You have completed the quest");
		giveRewards(p);
	}

	/**
	 * @see Quest#requirementsLore()
	 */
	@Override
	public List<String> requirementsLore() {
		List<String> lore = new ArrayList<>();
		for (String s : NPCList.keySet()) {
			if (hasTalkedTo.contains(s)) {
				lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.GREEN + "\u2714 Talked To " + s);
			} else {
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + "\u2715 " + ChatColor.RED + "Talk To " + s);
			}
		}
		return lore;
	}

	/**
	 * @see Quest#saveQuest()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveQuest() {
		JSONObject jo = new JSONObject();
		JSONArray hasTalkedTo = new JSONArray();
		for (String talkedTo : getHasTalkedTo()) {
			hasTalkedTo.add(talkedTo);
		}
		jo.put("TalkedTo", hasTalkedTo);
		return jo;
	}

	/**
	 * @see Quest#loadQuestToPlayer(JSONObject)
	 */
	@Override
	public void loadQuestToPlayer(JSONObject jo) {
		if (jo.containsKey("TalkedTo")) {
			JSONArray talkedTo = (JSONArray) jo.get("TalkedTo");
			for (Object o : talkedTo) {
				String s = o.toString();
				getHasTalkedTo().add(s);
			}
		}
	}

	/**
	 * @see Quest#loadQuest(JSONObject)
	 */
	@Override
	public void loadQuest(JSONObject jo) {
		JSONArray dialog = (JSONArray) jo.get("NPCS");
		for (Object o : dialog) {
			JSONObject npc = (JSONObject) o;
			String npcName = npc.get("Name").toString();
			addNPC(npcName);
			if (npc.containsKey("Dialog")) {
				JSONArray dialogStrings = (JSONArray) npc.get("Dialog");
				for (Object dialogString : dialogStrings) {
					String s = dialogString.toString();
					addDialog(npcName, s);
				}
			}
		}
	}

}