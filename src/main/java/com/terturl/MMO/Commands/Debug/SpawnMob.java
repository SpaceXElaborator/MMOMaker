package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.Framework.CraftCommand;

public class SpawnMob extends CraftCommand {

	public SpawnMob() {
		super("mmomob");
	}
	
	public void handleCommand(Player p, String[] args) {
//		MMOMob mob = MinecraftMMO.getInstance().getMobManager().getMobs().get(0);
//		mob.setLocation(p.getLocation());
//		mob.generateArmorStandParts();
//		mob.spawnEntity(p);
	}
	
}