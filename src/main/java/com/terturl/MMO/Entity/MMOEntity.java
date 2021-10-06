package com.terturl.MMO.Entity;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.level.World;

public class MMOEntity extends EntityCreature {
	
	private String name;
	private EntityTypes<? extends EntityCreature> type;
	
	protected MMOEntity(EntityTypes<? extends EntityCreature> t, World w, String s) {
		super(t, w);
		setCustomName(new ChatComponentText(s));
		name = s;
		type = t;
	}
	
	@Override
	public void initPathfinder() {
		this.bP.a(1, new PathfinderGoalFloat(this));
		this.bP.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.bQ.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
	}
	
	public MMOEntity clone() {
		MMOEntity me = new MMOEntity(type, this.getWorld(), name);
		return me;
	}
	
}