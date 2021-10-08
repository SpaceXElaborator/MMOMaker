package com.terturl.MMO.Player.Skills.Crafting;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Util.Events.MMOItemCraftEvent;
import com.terturl.MMO.Util.Items.CustomItem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class MMORecipe {
	
	@Getter
	private String name;
	
	@Getter
	private CustomItem product;
	
	@Getter
	private Integer levelRequired;
	
	@Getter @Setter
	private Integer amountToGive;
	
	@Getter @Setter
	private Double xpGiven = 0.0;
	
	@Getter
	private Map<CustomItem, Integer> recipeMapping = new HashMap<>();
	
	public MMORecipe(String n, CustomItem ci, Integer level) {
		product = ci;
		levelRequired = level;
		name = n;
	}
	
	public void addItem(CustomItem ci, Integer amount) {
		recipeMapping.put(ci, amount);
	}
	
	public void craftItem(Player p) {
		if(!PlayerHasAllItems(p)) return;
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		if(mc.getCraftSkill().getLevel() < levelRequired) return;
		for(CustomItem ci : recipeMapping.keySet()) {
			ItemStack is = ci.makeItem(recipeMapping.get(ci));
			p.getInventory().remove(is);
		}
		mc.getCraftSkill().addXP(xpGiven);
		p.getInventory().addItem(product.makeItem());
		p.updateInventory();
		MMOItemCraftEvent event = new MMOItemCraftEvent(product, p);
		if(!event.isCancelled()) Bukkit.getPluginManager().callEvent(event);
	}
	
	public boolean PlayerHasAllItems(Player p) {
		for(CustomItem ci : recipeMapping.keySet()) {
			ItemStack is = ci.makeItem();
			if(!p.getInventory().containsAtLeast(is, recipeMapping.get(ci))) {
				return false;
			}
		}
		return true;
	}
	
}