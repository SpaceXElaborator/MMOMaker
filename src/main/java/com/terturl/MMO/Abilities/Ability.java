package com.terturl.MMO.Abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Effects.Effect.LocationType;
import com.terturl.MMO.Effects.Util.EffectInformation;

import lombok.Getter;
import lombok.Setter;

public class Ability implements Cloneable {

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private Integer requiredLevel;

	@Getter
	@Setter
	private Map<AbilityCosts, Double> costs = new HashMap<>();

	@Getter
	private List<Effect> effects = new ArrayList<>();

	public Ability(String n) {
		name = n;
	}

	public void addCost(AbilityCosts ac, Double Amount) {
		costs.put(ac, Amount);
	}

	public void useAbility(Player p) {
		getEffects().stream().forEach(e -> {
			EffectInformation ei = e.getEffectInformation();
			ei.setPlayer(p);
			if (ei.getLocType().equals(LocationType.PLAYER)) {
				ei.setLoc(p.getLocation().clone());
			} else if (ei.getLocType().equals(LocationType.POINT)) {
				Location loc = p.getEyeLocation().clone();
				Vector inFrontOf = loc.getDirection().normalize().multiply(ei.getRange());
				ei.setLoc(loc.add(inFrontOf));
			} else if (ei.getLocType().equals(LocationType.BLOCKAT)) {
				ei.setLoc(p.getTargetBlock((Set<Material>) null, 10).getLocation().add(0.5, 0.5, 0.5).clone());
			}
			e.setEffectInformation(ei);
			((Effect)e.clone()).run();
		});
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addEffect(Effect e) {
		effects.add(e);
	}

	public enum AbilityCosts {
		HEALTH, MANA, MONEY;
	}

}