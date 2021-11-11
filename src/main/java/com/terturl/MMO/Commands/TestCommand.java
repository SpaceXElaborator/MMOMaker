package com.terturl.MMO.Commands;

import org.bukkit.entity.Player;

import com.terturl.MMO.Commands.Console.ShowAbilities;
import com.terturl.MMO.Commands.Console.ShowItems;
import com.terturl.MMO.Commands.Debug.Crafting;
import com.terturl.MMO.Commands.Debug.GetRecipe;
import com.terturl.MMO.Commands.Debug.GetSkill;
import com.terturl.MMO.Commands.Debug.RemoveClass;
import com.terturl.MMO.Commands.Debug.SchematicCommand;
import com.terturl.MMO.Commands.Debug.ShopCommand;
import com.terturl.MMO.Commands.Debug.SpawnClass;
import com.terturl.MMO.Commands.Debug.SpawnItem;
import com.terturl.MMO.Commands.Debug.SpawnMMOEntity;
import com.terturl.MMO.Commands.Debug.SpawnMob;
import com.terturl.MMO.Commands.Debug.SpawnSkull;
import com.terturl.MMO.Commands.Debug.TestParticles;
import com.terturl.MMO.Commands.Debug.UseAbility;
import com.terturl.MMO.Framework.CraftCommand;

public class TestCommand extends CraftCommand {

	public TestCommand() {
		super("mmo-test");
		addSubCommand(new SpawnClass(), new RemoveClass(), new SpawnItem(), new SpawnSkull(),
				new TestParticles(), new UseAbility(), new ShowItems(), new ShowAbilities(), new ShopCommand(),
				new Crafting(), new SpawnMMOEntity(), new GetRecipe(), new GetSkill(), new SpawnMob(), new SchematicCommand());
	}

	public void handleCommand(Player p, String[] args) {
		if (!p.isOp())
			return;
		p.sendMessage("Please follow with a sub-command");
	}

}