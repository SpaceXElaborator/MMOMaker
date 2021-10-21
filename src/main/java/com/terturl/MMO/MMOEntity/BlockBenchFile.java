package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOTexture;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BlockBenchFile {

	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private Integer width;
	
	@Getter @Setter
	private Integer height;
	
	@Getter @Setter
	private Double eyeHeight;
	
	@Getter
	private List<BBOCube> elements = new ArrayList<>();
	
	@Getter
	private List<BBOOutliner> outliner = new ArrayList<>();
	
	@Getter
	private List<BBOTexture> textures = new ArrayList<>();
	
	public BBOCube findCubeByUUID(UUID uuid) {
		return elements.stream().filter(e -> e.getUuid().equals(uuid)).findFirst().orElse(null);
	}
	
	public BBOOutliner findOutlinerByUUID(UUID uuid) {
		return outliner.stream().filter(e -> e.getUuid().equals(uuid)).findFirst().orElse(null);
	}
	
	public boolean isOutliner(UUID uuid) {
		for(BBOOutliner outliner : outliner) {
			if(outliner.getUuid().equals(uuid)) return true;
		}
		return false;
	}
	
}