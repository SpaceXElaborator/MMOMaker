package com.terturl.MMO.Dungeon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class DungeonSchematicBlockInformation {

	@Getter @Setter
	private Location dungeonSchematicBlockLocation;
	
	@Getter @Setter
	private Material dungeonShematicBlockMaterial;
	
	@Getter @Setter
	private BlockData dungeonSchematicBlockBlockData;
	
}