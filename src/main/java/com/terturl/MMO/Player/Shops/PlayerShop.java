package com.terturl.MMO.Player.Shops;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.terturl.MMO.MinecraftMMO;

import lombok.Getter;
import lombok.Setter;

public class PlayerShop implements Listener {

	@Getter @Setter
	private UUID playerOwner;
	
	@Getter @Setter
	private List<ShopItem> itemsForSale = new ArrayList<>();
	
	@Getter @Setter
	private Location shopLocation;
	
	public PlayerShop(UUID uuid, Location loc) {
		playerOwner = uuid;
		shopLocation = loc;
		MinecraftMMO.getInstance().registerListener(this);
		createShop();
	}
	
	private void createShop() {
		MinecraftMMO.getInstance().getServer().getConsoleSender().sendMessage("Player Shop Created");
		getShopLocation().getBlock().setType(Material.CHEST);
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(!e.getClickedBlock().getType().equals(Material.CHEST)) return;
		if(!e.getClickedBlock().getLocation().equals(shopLocation)) return;
		e.getPlayer().sendMessage("Shop Block");
	}
	
}