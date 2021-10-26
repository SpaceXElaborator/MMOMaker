package com.terturl.MMO.Inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Framework.ClickAction;
import com.terturl.MMO.Framework.InventoryButton;
import com.terturl.MMO.Framework.InventoryUI;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Quests.Quest;

/**
 * An inventory that is presented to the player when they interact with the
 * compass for viewing quests
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class QuestInventory extends InventoryUI {

	/**
	 * Creates the quest inventory based on the MMOPlayer given and update the
	 * inventory
	 * 
	 * @param mp MMOPlayer to check for quests
	 */
	public QuestInventory(MMOPlayer mp) {
		super(18, "Quests");
		for (Quest q : mp.getMmoClasses().get(mp.getCurrentCharacter()).getActiveQuests()) {
			ItemStack item = q.questItem();
			addButton(new InventoryButton(item) {
				@Override
				public void onPlayerClick(final Player p, ClickAction a) {
					// Can interact with Quest to get the NPC that gave them the quest, needs to be
					// fixed for quests that do not have an NPC that gave them the quest
					p.setCompassTarget(MinecraftMMO.getInstance().getNpcHandler().findNpcWithQuest(q).getLocation());
				}
			});
		}
		updateInventory();
	}

}