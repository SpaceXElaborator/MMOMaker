package com.terturl.MMO.Inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Abilities.Ability;
import com.terturl.MMO.Framework.ClickAction;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;

import net.md_5.bungee.api.ChatColor;

public class AbilityViewer extends InventoryUI {

	public AbilityViewer(Player p) {
		super(27, "Abilities");
		
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		
		for(String s : mc.getPlayerAbilities()) {
			Ability a = MinecraftMMO.getInstance().getAbilityManager().getAbility(s);
			if(a == null) continue;
			ItemStack abilityItem = makeItem(a);
			addButton(new InventoryButton(abilityItem) {
				@Override
				public void onPlayerClick(Player p, ClickAction action) {
					
				}
			});
		}
		updateInventory();
	}
	
	private ItemStack makeItem(Ability a) {
		ItemStack i = new ItemStack(Material.PAPER);
		
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + a.getName());
		
		List<String> lore = new ArrayList<>();
		lore.add("");
		
		return i;
	}
	
}