package com.terturl.MMO.Dungeon;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Dungeon {

	@Getter
	private String dungeonName;
	
	@Getter
	private List<DungeonBlock> dungeonBlocks = new ArrayList<>();
	
	public Dungeon(String s) {
		dungeonName = s;
	}
	
	public void addDungeonBlock(DungeonBlock db) {
		dungeonBlocks.add(db);
	}
	
}