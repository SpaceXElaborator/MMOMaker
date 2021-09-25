package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class SpawnItem extends CraftCommand {

	public SpawnItem() {
		super("getitem");
	}
	
	public void handleCommand(Player p, String[] args) {
		p.getInventory().addItem(MinecraftMMO.getInstance().getItemManager().getItem(args[0]).makeItem());
		p.updateInventory();
	}
	
}