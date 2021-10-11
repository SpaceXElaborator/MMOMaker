package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Player.Skills.Herbalism.Herbalism;

public class GetSkill extends CraftCommand {

	public GetSkill() {
		super("get-skill");
	}
	
	public void handleCommand(Player p, String[] args) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		mc.getPlayerSkills().add(new Herbalism());
		p.sendMessage("Added skill: Herbalism");
	}
	
}