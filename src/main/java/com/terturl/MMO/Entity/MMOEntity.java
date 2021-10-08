package com.terturl.MMO.Entity;

import java.util.ArrayList;
import java.util.List;

import com.terturl.MMO.Entity.Util.MMOEntityDrop;
import com.terturl.MMO.Util.SoundInformation;
import com.terturl.MMO.Util.Math.MinMax;

import lombok.Getter;
import lombok.Setter;
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
	
	@Getter @Setter
	private List<MMOEntityDrop> entityDrops = new ArrayList<>();
	
	@Getter @Setter
	private MinMax givableXP;
	
	@Getter @Setter
	private MinMax givableCurrency;
	
	@Getter @Setter
	private SoundInformation MMOSoundHurt, MMOSoundDeath;
	
	protected MMOEntity(EntityTypes<? extends EntityCreature> t, World w, String s) {
		super(t, w);
		setCustomName(new ChatComponentText(s));
		setCustomNameVisible(true);
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
		me.setGivableCurrency(givableCurrency);
		me.setGivableXP(givableXP);
		me.setEntityDrops(entityDrops);
		me.setMMOSoundDeath(getMMOSoundDeath());
		me.setMMOSoundHurt(getMMOSoundHurt());
		return me;
	}
	
}