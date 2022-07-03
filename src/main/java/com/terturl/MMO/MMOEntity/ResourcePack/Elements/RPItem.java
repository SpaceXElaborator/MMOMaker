package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * A Java representation of a Minecraft resource pack item JSON file
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class RPItem {

	@Getter
	private int data = 0;

	@Getter
	private String parent = "item/generated";

	@Getter
	private Map<String, String> textures = new HashMap<>();

	@Getter
	private List<RPPredicate> overrides = new ArrayList<>();

	/**
	 * Creates a new item and presets the default layer0 to item/saddle
	 */
	public RPItem() {
		textures.put("layer0", "item/saddle");
	}

	/**
	 * Will create a new override with model name being comprised of the mob's name
	 * and the part's name with data being the override's predicates'
	 * CustomModelData
	 * 
	 * @param mobName Name of the mob
	 * @param part Name of the part being added
	 */
	public void addOverride(String mobName, String part) {
		data += 1;
		overrides.add(new RPPredicate(data, "mmorpg:" + mobName + "/" + part));
	}

}