package com.terturl.MMO.Effects.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.terturl.MMO.Effects.Effect.EffectType;
import com.terturl.MMO.Effects.Effect.LocationType;

import lombok.Getter;
import lombok.Setter;

public class EffectInformation  {

	@Getter
	private List<Entity> damaged = new ArrayList<>();
	
	@Getter @Setter
	private List<SoundInformation> sounds = new ArrayList<>();
	
	@Getter @Setter
	private EffectType type;
	
	@Getter @Setter
	private LocationType locType;
	
	@Getter @Setter
	private long delay;
	
	@Getter @Setter
	private double damage;
	
	@Getter @Setter
	private Location loc;
	
	@Getter @Setter
	private long every;
	
	@Getter @Setter
	private int particleAmount;
	
	@Getter @Setter
	private long duration;
	
	@Getter @Setter
	private Player player;
	
	@Getter @Setter
	private Particle particle;
	
	@Getter @Setter
	private double xOff = 0.0;
	@Getter @Setter
	private double yOff = 0.0;
	@Getter @Setter
	private double zOff = 0.0;
	
	@Getter @Setter
	private double range = 0.0;
	
	@Getter @Setter
	private int limitTimes;
	
	public EffectInformation clone() {
		EffectInformation ei = new EffectInformation();
		ei.getDamaged().clear();
		List<SoundInformation> sounds = new ArrayList<>();
		sounds.addAll(getSounds());
		ei.setSounds(sounds);
		ei.setLocType(getLocType());
		ei.setType(getType());
		ei.setDelay(getDelay());
		ei.setDamage(getDamage());
		ei.setLoc(getLoc());
		ei.setEvery(getEvery());
		ei.setParticleAmount(getParticleAmount());
		ei.setDuration(getDuration());
		ei.setPlayer(getPlayer());
		ei.setParticle(getParticle());
		ei.setRange(getRange());
		ei.setXOff(getXOff());
		ei.setYOff(getYOff());
		ei.setZOff(getZOff());
		ei.setLimitTimes(ei.getLimitTimes());
		return ei;
	}
	
}