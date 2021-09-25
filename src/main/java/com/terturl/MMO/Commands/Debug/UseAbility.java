package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Abilities.Ability;
import com.terturl.MMO.Framework.CraftCommand;

public class UseAbility extends CraftCommand {

	public UseAbility() {
		super("ability");
	}
	
	public void handleCommand(Player p, String[] args) {
		Ability a = (Ability) MinecraftMMO.getInstance().getAbilityManager().getAbilities().get(args[0]).clone();
		a.useAbility(p);
	}
	
}