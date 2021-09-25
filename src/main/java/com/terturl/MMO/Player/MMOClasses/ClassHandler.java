package com.terturl.MMO.Player.MMOClasses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Quests.Quest;

import lombok.Getter;

public class ClassHandler {

	@Getter
	private List<MMOClass> classes = new ArrayList<>();
	
	public void registerClass(MMOClass cl) {
		for(MMOClass mc : classes) {
			if(mc.equals(cl)) return;
		}
		classes.add(cl);
	}
	
	public void addQuest(Player p, Quest q) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		mc.getActiveQuests().add(q);
		mp.updateNPCQuests();
	}
	
	public void selectClass(Player p, String mmoClass) {
		if (!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(p)) {
			// MinecraftMMO.getInstance().getPlayerHandler().createPlayerFile(p);
			MMOPlayer mp = new MMOPlayer(p);
			MMOClass mc = (MMOClass) getClass(mmoClass).clone();
			mp.getMmoClasses().add(mc);
			mp.setCurrentCharacter(mp.getMmoClasses().size()-1);
			MinecraftMMO.getInstance().getPlayerHandler().addPlayer(mp);
			p.getInventory().clear();
			p.getInventory().setBoots(mc.starterBoots());
			p.getInventory().setChestplate(mc.starterChestplate());
			p.getInventory().setLeggings(mc.starterLeggings());
			p.getInventory().setHelmet(mc.starterHelmet());
			p.getInventory().setItemInOffHand(mc.startOffHand());
			p.getInventory().setItem(0, mc.startMainHand());
			p.updateInventory();
		}
	}
	
	public MMOClass getClass(String name) {
		return classes.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
	}
	
	public boolean containsClass(String s) {
		MMOClass mmoClass = classes.stream().filter(e -> e.getName().equals(s)).findFirst().orElse(null);
		if(mmoClass == null) return false;
		return true;
	}
	
}