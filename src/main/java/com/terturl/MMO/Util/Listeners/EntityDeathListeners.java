package com.terturl.MMO.Util.Listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.MMOEntityDeathEvent;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.EntityKillQuest;

/**
 * 
 * @author Sean Rahman
 * @since 0.29.0
 * @version 
 */
public class EntityDeathListeners implements Listener {

	@EventHandler
	public void EntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getKiller();
			MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
			if(mp == null) return;
			MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
			
			// Filter out all KillEntity quests into one list
			List<Quest> killQuests = mc.getQuestsWithType("KillEntity");
			
			// Get the type of entity that was killed and the add the amount to the player's quest's list and check for completion
			EntityType et = e.getEntity().getType();
			for(Quest q : killQuests) {
				EntityKillQuest ekq = (EntityKillQuest)q;
				if(!ekq.getAmountToKill().containsKey(et)) continue;
				ekq.getHasKilled().put(et, ekq.getHasKilled().get(et) + 1);
				if(ekq.hasComplete(p)) {
					ekq.finishQuest(p);
				}
			}
		}
	}
	
	@EventHandler
	public void MMODeathEvent(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof LivingEntity)) return;
		if(!(e.getEntity().getKiller() instanceof Player)) return;
		Entity en = e.getEntity();
		Player p = e.getEntity().getKiller();
		
		// Check for if the entity killed is contained in the list of alive custom entities within the EntityManager
		// Create a new event and have Bukkit call the event
		if(MinecraftMMO.getInstance().getEntityManager().containsUUID(en.getUniqueId())) {
			MMOEntityDeathEvent mede = new MMOEntityDeathEvent(MinecraftMMO.getInstance().getEntityManager().getEntity(en.getUniqueId()), p);
			if(!mede.isCancelled()) Bukkit.getPluginManager().callEvent(mede);
		}
	}
	
}