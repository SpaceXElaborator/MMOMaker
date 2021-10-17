package com.terturl.MMO.MMOEntity.ResourcePack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terturl.MMO.MMOEntity.MMOMobEntity;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Cube;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Display;

import lombok.Getter;

public class MobBoneFile {

	@Getter
	private String file;
	
	@Getter
	private Map<String, String> textures = new HashMap<>();
	
	@Getter
	private List<Cube> elements = new ArrayList<>();
	
	@Getter
	private Map<String, Display> display = new HashMap<>();
	
	public MobBoneFile(MMOMobEntity mob, String name) {
		file = name;
		Display head = new Display();
		head.setScale(3.73D, 3.73D, 3.73D);
		display.put("head", head);
		
		mob.getBbf().getTextures().forEach(e -> {
			textures.put(String.valueOf(e.getId()), "mmorpg:entity/" + e.getName().toLowerCase());
		});
	}
	
	public boolean normalize() {
		Double[] offset = {0.0D, 0.0D, 0.0D};
		Map<Cube, Cube> cubes = new HashMap<>();
		elements.forEach(original -> {
			Cube cube = new Cube();
			cube.setFrom(original.getFrom()[0], original.getFrom()[1], original.getFrom()[2]);
			cube.setTo(original.getTo()[0], original.getTo()[1], original.getTo()[2]);
			cube.shrinkCube(0.6D);
			cubes.put(cube, original);
			for(int i = 0; i < 3; i++) {
				if(cube.getFrom()[i] + offset[i] > 32.0D) offset[i] = offset[i] - cube.getFrom()[i] + offset[i] - 32.0D;
				if(cube.getFrom()[i] + offset[i] < -16.0D) offset[i] = offset[i] - cube.getFrom()[i] + offset[i] + 16.0D;
				if(cube.getTo()[i] + offset[i] > 32.0D) offset[i] = offset[i] - cube.getTo()[i] + offset[i] - 32.0D;
				if(cube.getTo()[i] + offset[i] < -16.0D) offset[i] = offset[i] - cube.getTo()[i] + offset[i] + 16.0D;
			}
		});
		
		for(Cube cube : cubes.keySet()) {
			boolean from = cube.addFrom(offset);
			boolean to = cube.addTo(offset);
			if(!from || !to) return shrinkLarge();
		}
		
		cubes.forEach((cube, original) -> {
			original.setFrom(cube.getFrom()[0], cube.getFrom()[1], cube.getFrom()[2]);
			original.setTo(cube.getTo()[0], cube.getTo()[1], cube.getTo()[2]);
			original.getRotation().shrinkOrigin(0.6D);
			original.getRotation().addOrigin(offset);
		});
		if(offset[0] != 0.0D || offset[1] != 0.0D || offset[2] != 0.0D) display.get("head").moveTranslation(offset);
		display.get("head").setScale(3.8095D, 3.8095D, 3.8095D);
		return true;
	}
	
	private boolean shrinkLarge() {
		Double[] offset = {0.0D, 0.0D, 0.0D};
		elements.forEach(cube -> {
			cube.shrinkCube(0.42857143D);
			for(int i = 0; i < 3; i++) {
				if(cube.getFrom()[i] + offset[i] > 32.0D) offset[i] = offset[i] - cube.getFrom()[i] + offset[i] - 32.0D;
				if(cube.getFrom()[i] + offset[i] < -16.0D) offset[i] = offset[i] - cube.getFrom()[i] + offset[i] + 16.0D;
				if(cube.getTo()[i] + offset[i] > 32.0D) offset[i] = offset[i] - cube.getTo()[i] + offset[i] - 32.0D;
				if(cube.getTo()[i] + offset[i] < -16.0D) offset[i] = offset[i] - cube.getTo()[i] + offset[i] + 16.0D;
			}
		});
		elements.forEach(cube -> {
			if(cube.addFrom(offset) && cube.addTo(offset)) {
				cube.getRotation().shrinkOrigin(0.42857143D);
				cube.getRotation().addOrigin(offset);
				return;
			}
		});
		if(offset[0] != 0.0D || offset[1] != 0.0D || offset[2] != 0.0D) display.get("head").moveTranslation(offset);
		display.get("head").setScale(3.73D, 3.73D, 3.73D);
		return false;
	}
	
}