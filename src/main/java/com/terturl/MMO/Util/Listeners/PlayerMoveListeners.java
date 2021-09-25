package com.terturl.MMO.Util.Listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Quest.QuestType;
import com.terturl.MMO.Quests.Subquests.LocationQuest;

public class PlayerMoveListeners implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		//if(!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(e.getPlayer())) return;
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer());
		if(mp == null) return;
//		if(mp.isHasUpdated()) {
//			mp.setHasUpdated(true);
//			mp.updateNPCQuests();
//		}
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		List<Quest> moveQuests = mc.getActiveQuests().stream().filter(q -> q.getType().equals(QuestType.LOCATION)).collect(Collectors.toList());
		for(Quest q : moveQuests) {
			LocationQuest lq = (LocationQuest)q;
			if(lq.hasComplete(e.getPlayer())) {
				lq.completeQuest(e.getPlayer());
				mc.getCompletedQuests().add(q.getName());
				mc.getActiveQuests().remove(q);
				mp.updateNPCQuests();
			}
		}
	}
	
}