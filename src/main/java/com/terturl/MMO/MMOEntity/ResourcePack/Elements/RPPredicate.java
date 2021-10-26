package com.terturl.MMO.MMOEntity.ResourcePack.Elements;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A Java representation of an Override from Minecraft's resource pack
 * @author Sean Rahman
 *
 */
@AllArgsConstructor
public class RPPredicate {

	@Getter
	private Integer customModelData;
	
	@Getter
	private String model;
	
}