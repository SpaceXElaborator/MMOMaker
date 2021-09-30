package com.terturl.MMO.Util.Listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Quest.QuestType;
import com.terturl.MMO.Quests.Subquests.EntityKillQuest;

public class EntityDeathListeners implements Listener {

	@EventHandler
	public void EntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getKiller();
			MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
			if(mp == null) return;
			MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
			List<Quest> killQuests = mc.getActiveQuests().stream().filter(q -> q.getType().equals(QuestType.KILLENTITY)).collect(Collectors.toList());
			EntityType et = e.getEntity().getType();
			for(Quest q : killQuests) {
				EntityKillQuest ekq = (EntityKillQuest)q;
				if(!ekq.getAmountToKill().containsKey(et)) continue;
				ekq.getHasKilled().put(et, ekq.getHasKilled().get(et) + 1);
				if(ekq.hasComplete(p)) {
					ekq.completeQuest(p);
					mc.getCompletedQuests().add(q.getName());
					mc.getActiveQuests().remove(q);
					mp.updateNPCQuests();
				}
			}
		}
	}
	
}