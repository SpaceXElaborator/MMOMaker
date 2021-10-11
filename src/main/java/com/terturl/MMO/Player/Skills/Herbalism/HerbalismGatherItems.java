package com.terturl.MMO.Player.Skills.Herbalism;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.terturl.MMO.Util.MMODropChance;
import com.terturl.MMO.Util.Items.CustomItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class HerbalismGatherItems {

	@Getter
	private Material mat;
	
	@Getter
	private Double xp;
	
	@Getter
	private Map<CustomItem, MMODropChance> items = new HashMap<>();
	
}