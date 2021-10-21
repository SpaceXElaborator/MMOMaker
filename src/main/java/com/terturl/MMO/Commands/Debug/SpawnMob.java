package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class SpawnMob extends CraftCommand {

	public SpawnMob() {
		super("mmomob");
	}
	
	public void handleCommand(Player p, String[] args) {		
		MinecraftMMO.getInstance().getMobManager().spawnMob(args[0], p.getLocation());
	}
	
}