package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Util.Items.SkullCreator;

public class SpawnSkull extends CraftCommand {

	public SpawnSkull() {
		super("skull");
	}
	
	public void handleCommand(Player p, String[] args) {
		p.getInventory().addItem(SkullCreator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlNTIyZDkxODI1MjE0OWU2ZWRlMmVkZjNmZTBmMmMyYzU4ZmVlNmFjMTFjYjg4YzYxNzIwNzIxOGFlNDU5NSJ9fX0="));
		p.updateInventory();
	}
	
}