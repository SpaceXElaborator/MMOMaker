package com.terturl.MMO.Commands.Debug;

import java.io.File;

import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Dungeon.Dungeon;
import com.terturl.MMO.Dungeon.DungeonBlock;
import com.terturl.MMO.Dungeon.DungeonSchematic;
import com.terturl.MMO.Framework.CraftCommand;

public class SchematicCommand extends CraftCommand {

	public SchematicCommand() {
		super("schematic");
	}
	
	public void handleCommand(Player p, String[] args) {
		DungeonSchematic dc = new DungeonSchematic(new File(MinecraftMMO.getInstance().getDataFolder(), "test.schem"));
		Dungeon d = new Dungeon("Tet");
		d.addDungeonBlock(new DungeonBlock(p.getLocation(), dc));
		d.pasteAll();
	}
	
}