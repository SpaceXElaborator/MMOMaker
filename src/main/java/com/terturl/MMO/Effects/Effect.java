package com.terturl.MMO.Effects;

import com.terturl.MMO.Effects.Util.EffectInformation;

import lombok.Getter;
import lombok.Setter;

public abstract class Effect implements Cloneable {

	@Getter @Setter
	private EffectInformation effectInformation;
	
	public Effect(EffectInformation info) {
		effectInformation = info;
	}
	
	public abstract void run();
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public enum EffectType {
		REPEATING, INFINITE, SINGLE, LIMIT;
	}
	
	public enum LocationType {
		PLAYER, POINT, BLOCKAT, POINTPLAYER;
	}
	
}