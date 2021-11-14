package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Inventories.AbilityViewer;

public class CheckAbilities extends CraftCommand {

	public CheckAbilities() {
		super("abilities");
	}
	
	public void handleCommand(Player p, String[] args) {
		AbilityViewer av = new AbilityViewer(p);
		av.open(p);
	}
	
}