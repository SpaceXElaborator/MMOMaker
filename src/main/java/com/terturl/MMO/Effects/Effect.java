package com.terturl.MMO.Effects;

import com.terturl.MMO.Effects.Util.EffectInformation;

import lombok.Getter;
import lombok.Setter;

/**
 * Skeleton class for an effect to run
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public abstract class Effect implements Cloneable {

	@Getter
	@Setter
	private EffectInformation effectInformation;

	public Effect(EffectInformation info) {
		effectInformation = info;
	}

	/**
	 * Abstract method to be ran when the ability fires
	 */
	public abstract void run();

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Repeating = The ability will run every x ticks for y amount of time
	 * Infinite = Will run until the player moves or takes damage
	 * Single = Run once
	 * Limit = The ability will fire x amount of times every y ticks
	 * @author Sean Rahman
	 *
	 */
	public enum EffectType {
		REPEATING, INFINITE, SINGLE, LIMIT;
	}

	/**
	 * Player = Location is based on player
	 * Point = Location is based at a point the player is looking at
	 * BLOCKAT/POINTPLAYER NOT USED
	 * @author Sean Rahman
	 *
	 */
	public enum LocationType {
		PLAYER, POINT, BLOCKAT, POINTPLAYER;
	}

}