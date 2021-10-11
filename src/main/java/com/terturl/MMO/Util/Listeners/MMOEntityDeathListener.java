package com.terturl.MMO.Util.Listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.MMOEntity;
import com.terturl.MMO.Entity.Util.MMOEntityDrop;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
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