package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class SpawnASMob extends CraftCommand {

	public SpawnASMob() {
		super("spawnasmob");
	}
	
	public void handleCommand(Player p, String[] args) {
		if(args.length == 0 || args.length > 1) return;
		if(args[0].equalsIgnoreCase("clear")) {
			MinecraftMMO.getInstance().getMobController().clear();
		} else if(args[0].equalsIgnoreCase("magmaboss")) {
			MinecraftMMO.getInstance().getMobController().spawnMagmaBoss(p.getLocation().getBlock().getLocation());
		}  else if(args[0].equalsIgnoreCase("rotate")) {
			MinecraftMMO.getInstance().getMobController().rotate();
		} else if(args[0].equalsIgnoreCase("start")) {
			MinecraftMMO.getInstance().getMobController().runWalk();
		} else if(args[0].equalsIgnoreCase("stop")) {
			MinecraftMMO.getInstance().getMobController().stopWalk();
		}
	}
	
}