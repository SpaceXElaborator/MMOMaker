package com.terturl.MMO.Dungeon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import lombok.Getter;
import lombok.Setter;

public class DungeonBlock {

	@Getter @Setter
	private DungeonSchematic dungeonSchematic;
	
	@Getter @Setter
	private Location dungeonBlockLocation;
	
	@Getter
	private List<DungeonSchematicBlockInformation> dungeonSchematicBlocks = new ArrayList<>();
	
	public DungeonBlock(Location loc, DungeonSchematic ds) {
		dungeonSchematic = ds;
		dungeonBlockLocation = loc;
		loadBlockMap(loc);
	}
	
	private void loadBlockMap(Location loc) {
		for(int x = 0; x < dungeonSchematic.getWidth(); x++) {
			for(int y = 0; y < dungeonSchematic.getHeight(); y++) {
				for(int z = 0; z < dungeonSchematic.getLength(); z++) {
					int index = y * dungeonSchematic.getWidth() * dungeonSchematic.getLength() + z * dungeonSchematic.getWidth() + x;
					int b = dungeonSchematic.getBlockIds()[index] & 0xFF;
					Location blockLoc = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
					String s = dungeonSchematic.getPalette().get(b);
					String mat = s.substring(s.indexOf(":"), s.indexOf("["));
					Material m = Material.valueOf(mat.toUpperCase());
					BlockData bd = Bukkit.getServer().createBlockData(s);
					dungeonSchematicBlocks.add(new DungeonSchematicBlockInformation(blockLoc, m, bd));
				}
			}
		}
	}
	
	public void pasteBlock() {
		
	}
	
}