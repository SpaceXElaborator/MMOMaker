package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class SpawnClass extends CraftCommand {

	public SpawnClass() {
		super("spawnclass");
	}
	
	public void handleCommand(Player p, String[] args) {
		MinecraftMMO.getInstance().getNpcHandler().spawnNPC(p.getLocation(), args[0]);
	}
	
}