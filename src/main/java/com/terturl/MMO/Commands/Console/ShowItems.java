package com.terturl.MMO.Commands.Console;

import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class ShowItems extends CraftCommand {

	public ShowItems() {
		super("show-items");
	}
	
	public void handleCommand(ConsoleCommandSender cs, String[] args) {
		StringJoiner sj = new StringJoiner(", ");
		MinecraftMMO.getInstance().getItemManager().getCustomItems().keySet().forEach(e -> {
			sj.add(e);
		});
		cs.sendMessage(ChatColor.GREEN + "Registered Items [" + sj.toString() + "]");
	}
	
}