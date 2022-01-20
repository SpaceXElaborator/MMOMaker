package com.terturl.MMO.Util.Listeners;

import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.ClickClassNPCEvent;
import com.terturl.MMO.API.Events.ClickNPCEvent;
import com.terturl.MMO.API.Events.ClickPlayerClassNPCEvent;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.NPCTalkQuest;

/**
 * Handles interacting with NPCs
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class InteractNPCListener implements Listener {

	@EventHandler
	public void handleNPCEvent(ClickNPCEvent e) {
		Player p = e.getP();
		NPC npc = e.getNpc();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if (mp == null)
			return;
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		List<Quest> talkToQuests = mc.getQuestsWithType("TalkTo");

		// NPC Talk Quests take priority over giving quests. Make sure there are no
		// quests to talk about first
		if (talkToQuests.size() > 0) {
			for (Quest q : talkToQuests) {
				NPCTalkQuest ntq = (NPCTalkQuest) q;
				if (!ntq.containsNPC(npc.getDisplayName()))
					continue;
				ntq.talkTo(npc.getDisplayName(), p);
				if (ntq.hasComplete(p)) {
					ntq.finishQuest(p);
				}
			}
		} else {
			npc.lookAtPlayer(p, p);
			String questToComplete = null;
			for (String q : mc.getCompletedableQuests()) {
				if (npc.getGivableQuest().contains(q)) {
					questToComplete = q;
					break;
				}
			}

			if (questToComplete != null) {
				MinecraftMMO.getInstance().getQuestManager().getQuest(questToComplete).completeQuest(p);
				mc.removeCompletableQuest(questToComplete);
				mc.getCompletedQuests().add(questToComplete);
				mc.removeActiveQuest(questToComplete);
				mp.updateNPCQuests();
				return;
			}

			if (npc.getGivableQuest().size() > 0) {
				String q = npc.getNextAvailabeQuest(MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p));

				if (q == null) {
					p.sendMessage(npc.getIdleString());
					return;
				}

				Quest quest = mp.addQuest(q);
				p.sendTitle(quest.getName(), quest.getPresentString(), 5, 10, 5);
				p.sendMessage(quest.getPresentString());
			} else {
				p.sendMessage(npc.getIdleString());
			}
		}
	}

	@EventHandler
	public void handleClassNPCEvent(ClickClassNPCEvent e) {
		Player p = e.getP();
		NPC npc = e.getNpc();
		npc.lookAtPlayer(p, p);
		// TODO: Check confirmation before selecting class
		// p.sendMessage("Want to select " + npc.getDisplayName() + " as your class?");
		MMOClass clazz = MinecraftMMO.getInstance().getClassHandler().getClass(npc.getDisplayName());
		MinecraftMMO.getInstance().getClassHandler().selectClass(p, npc.getDisplayName());
		MinecraftMMO.getInstance().getPlayerHandler().createPlayerFile(p);
		MinecraftMMO.getInstance().getPlayerHandler().giveBasicItems(p);
		AttributeInstance health = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		health.setBaseValue(clazz.getMaxHealth());
		p.setHealth(clazz.getMaxHealth());
		p.sendMessage("You have selected the class: " + npc.getDisplayName());
		MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p).updateNPCQuests();
	}

	@EventHandler
	public void handlePlayerClassEvent(ClickPlayerClassNPCEvent e) {
		NPC npc = e.getNpc();
		String s = npc.getDisplayName();
		Integer id = Integer.valueOf(s.substring(s.length() - 1, s.length()));
		MinecraftMMO.getInstance().getPlayerHandler().pickClass(e.getP(), id);
	}

}