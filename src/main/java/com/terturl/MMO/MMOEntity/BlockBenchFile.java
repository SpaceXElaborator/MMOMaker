package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;

import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BlockBenchFile {

	@Getter @Setter
	private String name;
	
	@Getter
	private List<BBOCube> elements = new ArrayList<>();
	
}