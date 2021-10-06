package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class SpawnMMOEntity extends CraftCommand {

	public SpawnMMOEntity() {
		super("spawn-entity");
	}
	
	public void handleCommand(Player p, String[] args) {
		MinecraftMMO.getInstance().getEntityManager().spawnEntity(p.getLocation(), MinecraftMMO.getInstance().getEntityManager().getEntity(args[0]));
	}
	
}