package com.terturl.MMO.Util.Listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.MMOEntity;
import com.terturl.MMO.Entity.MMOEntityDrop;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Quests.Subquests.MMOEntityKillQuest;
import com.terturl.MMO.Util.SoundInformation;
import com.terturl.MMO.Util.Events.MMOEntityDeathEvent;

public class MMOEntityDeathListener implements Listener {

	@EventHandler
	public void MmoEntityDeathEvent(MMOEntityDeathEvent e) {
		if(!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(e.getPlayer())) return;
		Player p = e.getPlayer();
		MMOEntity me = e.getMMOEntity();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		
		Double xp = me.getGivableXP().getRandomNumber();
		Double money = me.getGivableCurrency().getRandomNumber();
		mc.addMoney(money);
		mc.addXP(xp);
		
		List<Quest> killQuests = mc.getActiveQuests().stream().filter(q -> q.getQuestType().equals("MMOKillEntity")).collect(Collectors.toList());
		String s = e.getMMOEntity().getName();
		for(Quest q : killQuests) {
			MMOEntityKillQuest ekq = (MMOEntityKillQuest)q;
			if(!ekq.getAmountToKill().containsKey(s)) continue;
			ekq.getHasKilled().put(s, ekq.getHasKilled().get(s) + 1);
			if(ekq.hasComplete(p)) {
				ekq.finishQuest(p);
			}
		}
		
		World w = p.getWorld();
		for(MMOEntityDrop med : me.getEntityDrops()) {
			if(med.getsItem()) {
				ItemStack is = med.getCi().makeItem(med.getAmount());
				Item i = w.dropItemNaturally(new Location(p.getWorld(), me.locX(), me.locY(), me.locZ()), is);
				i.setCustomNameVisible(true);
				i.setPickupDelay(10);
			}
		}
		mc.updateClassInformation(p);
		
		SoundInformation si = me.getMMOSoundDeath();
		if(si == null) return;
		p.playSound(new Location(p.getWorld(), me.locX(), me.locY(), me.locZ()), si.getSound(), si.getVolume(), si.getPitch());
	}
	
}