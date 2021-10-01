package com.terturl.MMO.Player.Shops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import lombok.Getter;

public class ShopManager {
	
	@Getter
	private List<PlayerShop> playerShops = new ArrayList<>();
	
	public ShopManager() {}
	
	public void createPlayerShop(Player p, Location loc) {
		PlayerShop ps = new PlayerShop(p.getUniqueId(), loc);
		playerShops.add(ps);
	}
	
	public void removePlayerShop(PlayerShop ps) {
		if(playerShops.contains(ps)) {
			playerShops.remove(ps);
		}
	}
	
}