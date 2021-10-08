package com.terturl.MMO.Util.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.Events.ClickClassNPCEvent;
import com.terturl.MMO.Util.Events.ClickNPCEvent;
import com.terturl.MMO.Util.Events.ClickPlayerClassNPCEvent;

public class InteractNPCListener implements Listener {

	@EventHandler
	public void handleNPCEvent(ClickNPCEvent e) {
		Player p = e.getP();
		NPC npc = e.getNpc();
		npc.lookAtPlayer(p, p);
		if(npc.getGivableQuest().size() > 0) {
			// Eventually check for checks available
			Quest q = npc.getNextAvailabeQuest(MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p));
			
			if(q == null) {
				p.sendMessage(npc.getIdleString());
				return;
			}
			
			MinecraftMMO.getInstance().getClassHandler().addQuest(p, q);
			p.sendTitle(q.getName(), q.getPresentString(), 5, 10, 5);
			p.sendMessage(q.getPresentString());

		} else {
			p.sendMessage(npc.getIdleString());
		}
	}
	
	@EventHandler
	public void handleClassNPCEvent(ClickClassNPCEvent e) {
		Player p = e.getP();
		NPC npc = e.getNpc();
		npc.lookAtPlayer(p, p);
		// p.sendMessage("Want to select " + npc.getDisplayName() + " as your class?");
		MinecraftMMO.getInstance().getClassHandler().selectClass(p, npc.getDisplayName());
		MinecraftMMO.getInstance().getPlayerHandler().createPlayerFile(p);
		MinecraftMMO.getInstance().getPlayerHandler().addQuestItem(p);
		p.sendMessage("You have selected the class: " + npc.getDisplayName());
		MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p).updateNPCQuests();
	}
	
	@EventHandler
	public void handlePlayerClassEvent(ClickPlayerClassNPCEvent e) {
		NPC npc = e.getNpc();
		String s = npc.getDisplayName();
		Integer id = Integer.valueOf(s.substring(s.length()-1, s.length()));
		MinecraftMMO.getInstance().getPlayerHandler().pickClass(e.getP(), id);
	}
	
}