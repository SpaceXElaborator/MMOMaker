package com.terturl.MMO.MMOEntity.ResourcePack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terturl.MMO.MMOEntity.MMOMobEntity;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Cube;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Display;

import lombok.Getter;

/**
 * This will create a saddle.json file that will hold the information of how to
 * create a new model of each body part a MMOMob will contain. This will contain
 * all textures needed to create the mob visually and all locations of the mob's
 * cubes
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class MobBoneFile {

	@Getter
	private String file;

	@Getter
	private Map<String, String> textures = new HashMap<>();

	@Getter
	private List<Cube> elements = new ArrayList<>();

	@Getter
	private Map<String, Display> display = new HashMap<>();

	/**
	 * Will grab all textures needed from the MMOMobEntity and create a new file
	 * with the given name
	 * 
	 * @param mob
	 * @param name
	 * @see com.terturl.MMO.MMOEntity.MMOMobEntity
	 */
	public MobBoneFile(MMOMobEntity mob, String name) {
		file = name;
		Display head = new Display();
		head.setScale(3.73D, 3.73D, 3.73D);
		display.put("head", head);

		mob.getBbf().getTextures().forEach(e -> {
			textures.put(String.valueOf(e.getId()), "mmorpg:entity/" + e.getName().toLowerCase());
		});
	}

	/**
	 * The normalization processes will turn all BlockBench files into the standard
	 * Minecraft formation so that it can be cleany presented in Minecraft as well
	 * as checking for if the cube is to be a shrunken or enlarged based on the size
	 * of the Cube in BlockBench
	 * 
	 * @return If the cube was shrunken or enlarged
	 */
	public boolean normalize() {
		double[] offset = { 0.0D, 0.0D, 0.0D };
		Map<Cube, Cube> cubes = new HashMap<>();
		elements.forEach(original -> {
			Cube cube = new Cube();
			cube.setFrom(original.getFrom()[0], original.getFrom()[1], original.getFrom()[2]);
			cube.setTo(original.getTo()[0], original.getTo()[1], original.getTo()[2]);
			cube.shrinkCube(0.6D);
			cubes.put(cube, original);
			// This will go through each of the X,Y, and Z values to make sure that their
			// positions are cleanly and properly placed in the specifications of the
			// Minecraft work
			for (int i = 0; i < 3; i++) {
				if (cube.getFrom()[i] + offset[i] > 32.0D)
					offset[i] = offset[i] - cube.getFrom()[i] + offset[i] - 32.0D;
				if (cube.getFrom()[i] + offset[i] < -16.0D)
					offset[i] = offset[i] - cube.getFrom()[i] + offset[i] + 16.0D;
				if (cube.getTo()[i] + offset[i] > 32.0D)
					offset[i] = offset[i] - cube.getTo()[i] + offset[i] - 32.0D;
				if (cube.getTo()[i] + offset[i] < -16.0D)
					offset[i] = offset[i] - cube.getTo()[i] + offset[i] + 16.0D;
			}
		});

		for (Cube cube : cubes.keySet()) {
			boolean from = cube.addFrom(offset);
			boolean to = cube.addTo(offset);
			if (!from || !to)
				return shrinkLarge();
		}

		cubes.forEach((cube, original) -> {
			original.setFrom(cube.getFrom()[0], cube.getFrom()[1], cube.getFrom()[2]);
			original.setTo(cube.getTo()[0], cube.getTo()[1], cube.getTo()[2]);
			original.getRotation().shrinkOrigin(0.6D);
			original.getRotation().addOrigin(offset);
		});
		if (offset[0] != 0.0D || offset[1] != 0.0D || offset[2] != 0.0D)
			display.get("head").moveTranslation(offset);
		display.get("head").setScale(3.8095D, 3.8095D, 3.8095D);
		return true;
	}

	private boolean shrinkLarge() {
		double[] offset = { 0.0D, 0.0D, 0.0D };
		elements.forEach(cube -> {
			cube.shrinkCube(0.42857143D);
			// This will go through each of the X,Y, and Z values to make sure that their
			// positions are cleanly and properly placed in the specifications of the
			// Minecraft work
			for (int i = 0; i < 3; i++) {
				if (cube.getFrom()[i] + offset[i] > 32.0D)
					offset[i] = offset[i] - cube.getFrom()[i] + offset[i] - 32.0D;
				if (cube.getFrom()[i] + offset[i] < -16.0D)
					offset[i] = offset[i] - cube.getFrom()[i] + offset[i] + 16.0D;
				if (cube.getTo()[i] + offset[i] > 32.0D)
					offset[i] = offset[i] - cube.getTo()[i] + offset[i] - 32.0D;
				if (cube.getTo()[i] + offset[i] < -16.0D)
					offset[i] = offset[i] - cube.getTo()[i] + offset[i] + 16.0D;
			}
		});
		elements.forEach(cube -> {
			if (cube.addFrom(offset) && cube.addTo(offset)) {
				cube.getRotation().shrinkOrigin(0.42857143D);
				cube.getRotation().addOrigin(offset);
				return;
			}
		});
		if (offset[0] != 0.0D || offset[1] != 0.0D || offset[2] != 0.0D)
			display.get("head").moveTranslation(offset);
		display.get("head").setScale(3.73D, 3.73D, 3.73D);
		return false;
	}

}