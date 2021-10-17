package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;

import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BlockBenchFile {

	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String parent;
	
	@Getter
	private List<BBOCube> elements = new ArrayList<>();
	
	@Getter
	private List<BBOOutliner> outliner = new ArrayList<>();
	
}