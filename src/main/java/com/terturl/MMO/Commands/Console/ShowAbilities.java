package com.terturl.MMO.Commands.Console;

import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;

public class ShowAbilities extends CraftCommand {

	public ShowAbilities() {
		super("show-abilities");
	}
	
	public void handleCommand(ConsoleCommandSender cs, String[] args) {
		StringJoiner sj = new StringJoiner(", ");
		MinecraftMMO.getInstance().getAbilityManager().getAbilities().keySet().forEach(e -> {
			sj.add(e);
		});
		cs.sendMessage(ChatColor.GREEN + "Registered Abilities [" + sj.toString() + "]");
	}
	
}