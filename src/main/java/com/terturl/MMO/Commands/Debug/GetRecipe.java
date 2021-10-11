package com.terturl.MMO.Commands.Debug;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Player.Skills.Crafting.MMORecipe;

public class GetRecipe extends CraftCommand {

	public GetRecipe() {
		super("get-recipe");
	}

	public void handleCommand(Player p, String[] args) {
		MMORecipe mr = MinecraftMMO.getInstance().getRecipeManager().getRecipeByName(args[0]);
		if(mr == null) {
			p.sendMessage("Recipe Does Not Exist");
			return;
		}
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		mc.getCraftingRecipes().add(args[0]);
	}
	
}