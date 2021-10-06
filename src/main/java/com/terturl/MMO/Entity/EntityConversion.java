package com.terturl.MMO.Entity;

import lombok.Getter;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;

public enum EntityConversion {

	ZOMBIE(EntityTypes.be);

	@Getter
	private EntityTypes<? extends EntityCreature> type;
	
	EntityConversion(EntityTypes<? extends EntityCreature> be) {
		type = be;
	}
	
}