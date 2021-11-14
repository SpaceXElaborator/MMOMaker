package com.terturl.MMO.Dungeon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import com.terturl.MMO.Util.JSONHelpers.LocationUtils;

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
		// Go through the X Y and Z values
		for(int x = 0; x < dungeonSchematic.getWidth(); x++) {
			for(int y = 0; y < dungeonSchematic.getHeight(); y++) {
				for(int z = 0; z < dungeonSchematic.getLength(); z++) {
					// Index is taken from the Schematic Website: https://minecraft.fandom.com/wiki/Schematic_file_format
					// Under the "Blocks" Section
					int index = y * dungeonSchematic.getWidth() * dungeonSchematic.getLength() + z * dungeonSchematic.getWidth() + x;
					
					// Get the block numbers given in a Hex listing using only the buttom 4 bits
					int b = dungeonSchematic.getBlockIds()[index] & 0xFF;
					Location blockLoc = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
					String s = dungeonSchematic.getPalette().get(b);
					String mat = "";
					
					// Split the string from the JSON format: minecraft:{block}[Information]
					// This will split and grab just the block so that we can create the Material value
					if(s.contains("[")) {
						mat = s.substring(s.indexOf(":")+1, s.indexOf("["));
					} else {
						mat = s.substring(s.indexOf(":")+1);
					}
					
					// Create our Schematic Information to store the location, blockdata, and material to be pasted into the world
					Material m = Material.valueOf(mat.toUpperCase());
					BlockData bd = Bukkit.getServer().createBlockData(s);
					dungeonSchematicBlocks.add(new DungeonSchematicBlockInformation(blockLoc, m, bd));
				}
			}
		}
	}
	
	/**
	 * Paste the DungeonBlock into the world from its constructor Location
	 */
	public void pasteBlock() {
		for(DungeonSchematicBlockInformation dsbi : dungeonSchematicBlocks) {
			Bukkit.getConsoleSender().sendMessage(LocationUtils.locationSerializer(dsbi.getDungeonSchematicBlockLocation()));
			Block b = dsbi.getDungeonSchematicBlockLocation().getBlock();
			b.setType(dsbi.getDungeonShematicBlockMaterial());
			b.setBlockData(dsbi.getDungeonSchematicBlockBlockData());
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendBlockChange(dsbi.getDungeonSchematicBlockLocation(), dsbi.getDungeonSchematicBlockBlockData());
			}
		}
	}
	
}