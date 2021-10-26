package com.terturl.MMO.Player.Shops;

import java.util.UUID;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains information about the player who owns the item, the item to sale, and the price the player sets
 * @author Sean Rahman
 * @since 0.30.0
 *
 */
@AllArgsConstructor
public class ShopItem {

	@Getter @Setter
	private UUID itemOwner;
	
	@Getter
	private CustomItem itemForSale;
	
	@Getter @Setter
	private Double price;
	
}