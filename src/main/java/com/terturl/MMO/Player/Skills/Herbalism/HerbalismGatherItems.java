package com.terturl.MMO.Player.Skills.Herbalism;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.terturl.MMO.Util.MMODropChance;
import com.terturl.MMO.Util.Items.CustomItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Class that represents what can be gathered and what chance when clicking on
 * the material
 * 
 * @author Sean Rahman
 * @since 0.47.0
 *
 */
@AllArgsConstructor
@ToString
public class HerbalismGatherItems {

	/**
	 * The material to click on
	 */
	@Getter
	private Material mat;

	/**
	 * Amount of XP given to the player
	 */
	@Getter
	private Double xp;

	/**
	 * The CustomItem and dropchance that can drop
	 */
	@Getter
	private Map<CustomItem, MMODropChance> items = new HashMap<>();

}