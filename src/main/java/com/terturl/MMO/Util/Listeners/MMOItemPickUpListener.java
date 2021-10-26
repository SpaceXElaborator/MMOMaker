package com.terturl.MMO.Util.Listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.PickUpMMOItemEvent;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.MMOItemCollectQuest;
import com.terturl.MMO.Util.Items.CustomItem;

/**
 * Handles whenever a player picks up a CustomItem
 * 
 * @author Sean Rahman
 * @since 0.48.0
 *
 */
public class MMOItemPickUpListener implements Listener {

	@EventHandler
	public void pickUpItem(PickUpMMOItemEvent e) {
		Player p = e.getPlayer();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if (mp == null)
			return;
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());

		// Checks for all CollectItem quests and check to see if the item picked up
		// matches what they need to pick up
		List<Quest> collectQuests = mc.getQuestsWithType("CollectItem");
		CustomItem ci = e.getCustomItem();
		for (Quest q : collectQuests) {
			MMOItemCollectQuest cc = (MMOItemCollectQuest) q;
			if (!cc.getAmountToCollect().containsKey(ci.getName()))
				continue;
			cc.getHasCollected().put(ci.getName(), cc.getHasCollected().get(ci.getName()) + 1);
			if (cc.hasComplete(p)) {
				cc.finishQuest(p);
			}
		}
	}

}
