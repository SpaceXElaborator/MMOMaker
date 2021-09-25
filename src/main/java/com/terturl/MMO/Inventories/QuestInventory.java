package com.terturl.MMO.Inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.ClickAction;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Quests.Quest;

public class QuestInventory extends InventoryUI {

	public QuestInventory(MMOPlayer mp) {
		super(18, "Quests");
		for(Quest q : mp.getMmoClasses().get(mp.getCurrentCharacter()).getActiveQuests()) {
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta meta =item.getItemMeta();
			meta.setDisplayName(q.getName());
			item.setItemMeta(meta);
			addButton(new InventoryButton(item) {
				@Override
				public void onPlayerClick(final Player p, ClickAction a) {
					p.setCompassTarget(MinecraftMMO.getInstance().getNpcHandler().findNpcWithQuest(q).getLocation());
				}
			});
		}
		updateInventory();
	}
	
}