package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class ShopCommand extends CraftCommand {

	public ShopCommand() {
		super("shop");
	}
	
	public void handleCommand(Player p, String[] args) {
		MinecraftMMO.getInstance().getShopManager().createPlayerShop(p, p.getLocation().add(0, 1, 0));
	}
	
}