package com.terturl.MMO.Player.Shops;

import java.util.UUID;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ShopItem {

	@Getter @Setter
	private UUID itemOwner;
	
	@Getter
	private CustomItem itemForSale;
	
	@Getter @Setter
	private Double price;
	
}