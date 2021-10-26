package com.terturl.MMO.Util.Listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.MMOEntityDeathEvent;
import com.terturl.MMO.Entity.MMOEntity;
import com.terturl.MMO.Entity.MMOEntityDrop;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.MMOEntityKillQuest;
import com.terturl.MMO.Util.SoundInformation;

/**
 * Handles the event of killing a named MMOEntity
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class MMOEntityDeathListener implements Listener {

	@EventHandler
	public void MmoEntityDeathEvent(MMOEntityDeathEvent e) {
		if (!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(e.getPlayer()))
			return;
		Player p = e.getPlayer();
		MMOEntity me = e.getMMOEntity();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());

		Double xp = me.getGivableXP().getRandomNumber();
		Double money = me.getGivableCurrency().getRandomNumber();
		mc.addMoney(money);
		mc.addXP(xp);

		// Get all MMOKillEntity quests and check if the named MMOEntity is in that
		// quest and add 1 to the progress if it is present and check for completion of
		// quest
		List<Quest> killQuests = mc.getQuestsWithType("MMOKillEntity");
		String s = e.getMMOEntity().getName();
		for (Quest q : killQuests) {
			MMOEntityKillQuest ekq = (MMOEntityKillQuest) q;
			if (!ekq.getAmountToKill().containsKey(s))
				continue;
			ekq.getHasKilled().put(s, ekq.getHasKilled().get(s) + 1);
			if (ekq.hasComplete(p)) {
				ekq.finishQuest(p);
			}
		}

		// Drop all items by chance from the MMOEntity drop list
		World w = p.getWorld();
		for (MMOEntityDrop med : me.getEntityDrops()) {
			if (med.getsItem()) {
				ItemStack is = med.getCi().makeItem(med.getAmount());
				Item i = w.dropItemNaturally(new Location(p.getWorld(), me.locX(), me.locY(), me.locZ()), is);
				i.setCustomNameVisible(true);
				i.setPickupDelay(10);
			}
		}

		// Updated the MMOPlayer's MMOClass XP
		mc.updateClassInformation(p);

		SoundInformation si = me.getMMOSoundDeath();
		if (si == null)
			return;
		p.playSound(new Location(p.getWorld(), me.locX(), me.locY(), me.locZ()), si.getSound(), si.getVolume(),
				si.getPitch());
	}

}