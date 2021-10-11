package com.terturl.MMO.Util.Listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.CustomCraftQuest;
import com.terturl.MMO.Util.Events.MMOItemCraftEvent;
import com.terturl.MMO.Util.Items.CustomItem;

public class MMOItemCraftListener implements Listener {

	@EventHandler
	public void handleItemCraft(MMOItemCraftEvent e) {
		Player p = e.getPlayer();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if(mp == null) return;
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		List<Quest> craftQuests = mc.getActiveQuests().stream().filter(q -> q.getQuestType().equals("CraftItem")).collect(Collectors.toList());
		CustomItem ci = e.getCustomItem();
		for(Quest q : craftQuests) {
			CustomCraftQuest ccq = (CustomCraftQuest)q;
			if(!ccq.getToCraft().containsKey(ci.getName())) continue;
			ccq.getHasCrafted().put(ci.getName(), ccq.getHasCrafted().get(ci.getName()) + 1);
			if(ccq.hasComplete(p)) {
				ccq.completeQuest(p);
				mc.getCompletedQuests().add(q.getName());
				mc.getActiveQuests().remove(q);
				mp.updateNPCQuests();
			}
		}
	}
	
}