package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class RPItem {

	@Getter
	private Integer data = 0;
	
	@Getter
	private String parent = "item/generated";
	
	@Getter
	private Map<String, String> textures = new HashMap<>();
	
	@Getter
	private List<RPPredicate> overrides = new ArrayList<>();
	
	public RPItem() {
		textures.put("layer0", "item/saddle");
	}
	
	public void addOverride(String mobName, String part) {
		data += 1;
		overrides.add(new RPPredicate(data, "mmorpg:" + mobName + "/" + part));
	}
	
}