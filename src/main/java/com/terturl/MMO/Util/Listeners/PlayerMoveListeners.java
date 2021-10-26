package com.terturl.MMO.Util.Listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.LocationQuest;

/**
 * Handles all events related to the player moving
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class PlayerMoveListeners implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer());
		if (mp == null)
			return;
		if (mp.getCurrentCharacter() == -1)
			return;
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		List<Quest> moveQuests = mc.getQuestsWithType("Location");
		for (Quest q : moveQuests) {
			LocationQuest lq = (LocationQuest) q;
			if (lq.hasComplete(e.getPlayer())) {
				lq.finishQuest(e.getPlayer());
			}
		}
	}

}