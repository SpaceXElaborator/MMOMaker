package com.terturl.MMO.Entity;

import lombok.Getter;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;

/**
 * An enum to to turn net.minecraft.world entities into strings to be used for creation using JSON files
 * @author Sean Rahman
 * @since 0.33.0
 *
 */
public enum EntityConversion {

	ZOMBIE(EntityTypes.be);

	@Getter
	private EntityTypes<? extends EntityCreature> type;
	
	EntityConversion(EntityTypes<? extends EntityCreature> be) {
		type = be;
	}
	
}