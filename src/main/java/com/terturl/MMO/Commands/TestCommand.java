package com.terturl.MMO.Commands;

import org.bukkit.entity.Player;

import com.terturl.MMO.Commands.Console.ShowAbilities;
import com.terturl.MMO.Commands.Console.ShowItems;
import com.terturl.MMO.Commands.Debug.RemoveClass;
import com.terturl.MMO.Commands.Debug.SpawnASMob;
import com.terturl.MMO.Commands.Debug.SpawnClass;
import com.terturl.MMO.Commands.Debug.SpawnItem;
import com.terturl.MMO.Commands.Debug.SpawnSkull;
import com.terturl.MMO.Commands.Debug.TestParticles;
import com.terturl.MMO.Commands.Debug.UseAbility;
import com.terturl.MMO.Framework.CraftCommand;

public class TestCommand extends CraftCommand {

	public TestCommand() {
		super("mmo-test");
		addSubCommand(new SpawnClass(), new RemoveClass(), new SpawnItem(), new SpawnSkull(), new SpawnASMob(), new TestParticles(), new UseAbility(), new ShowItems(), new ShowAbilities());
	}
	
	public void handleCommand(Player p, String[] args) {
		if(!p.isOp()) return;
		p.sendMessage("Please follow with a sub-command");
	}
	
}