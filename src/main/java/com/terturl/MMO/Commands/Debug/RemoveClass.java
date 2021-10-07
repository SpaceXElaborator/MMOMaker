package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class RemoveClass extends CraftCommand {

	public RemoveClass() {
		super("removeclass");
	}
	
	public void handleCommand(Player p, String[] args) {
		MinecraftMMO.getInstance().getNpcHandler().removeNPC(p.getLocation(), args[0]);
	}
	
}