package com.terturl.MMO.Util.Listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.MMOItemCraftEvent;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.CustomCraftQuest;
import com.terturl.MMO.Util.Items.CustomItem;

/**
 * Listens for when a player crafts a CustomItem and works to update any and all
 * quests that have the item present
 * 
 * @author Sean Rahman
 * @since 0.43.0
 *
 */
public class MMOItemCraftListener implements Listener {

	@EventHandler
	public void handleItemCraft(MMOItemCraftEvent e) {
		Player p = e.getPlayer();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if (mp == null)
			return;
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		List<Quest> craftQuests = mc.getQuestsWithType("CraftItem");
		CustomItem ci = e.getCustomItem();
		for (Quest q : craftQuests) {
			CustomCraftQuest ccq = (CustomCraftQuest) q;
			if (!ccq.getToCraft().containsKey(ci.getName()))
				continue;
			ccq.getHasCrafted().put(ci.getName(), ccq.getHasCrafted().get(ci.getName()) + 1);
			if (ccq.hasComplete(p)) {
				ccq.finishQuest(p);
			}
		}

		// TODO: Add XP to MMOClass
	}

}