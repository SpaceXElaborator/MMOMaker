package com.terturl.MMO.Util.Items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.Util.Items.ItemEnums.MMOModifiers;

import lombok.Getter;
import lombok.Setter;

public class MMORunes {

	@Getter
	private String name;
	
	@Getter
	private ItemStack item;
	
	@Getter @Setter
	private Map<MMOModifiers, Double> modifiers;
	
	public MMORunes(String s, ItemStack is) {
		name = s;
		item = is;
		modifiers = new HashMap<MMOModifiers, Double>();
	}
	
	public MMORunes(String s, ItemStack is, Map<MMOModifiers, Double> mods) {
		this(s, is);
		modifiers = mods;
	}
	
}