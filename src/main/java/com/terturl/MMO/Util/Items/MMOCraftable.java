package com.terturl.MMO.Util.Items;

import java.util.Map;

import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;

public interface MMOCraftable {

	public CraftRarity getCraftingRarity();
	public Map<CustomItem, Integer> getCraftingRecipe();
	public void setCraftingRecipe(Map<CustomItem, Integer> recipe);
	
}