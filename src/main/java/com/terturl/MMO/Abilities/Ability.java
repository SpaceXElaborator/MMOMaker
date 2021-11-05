package com.terturl.MMO.Abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.terturl.MMO.Effects.Effect;

import lombok.Getter;
import lombok.Setter;

/**
 * Lays out the framework for how much an ability costs, the level requirement,
 * and what effects will be played when firing the ability
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
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

	/**
	 * Creates an empty ability, must add effects and costs
	 * 
	 * @param n Name of the ability
	 */
	public Ability(String n) {
		name = n;
	}

	/**
	 * Adds specified cost and amount to the ability.
	 * 
	 * @param ac     AbilityCosts type
	 * @param Amount Amount it will take
	 * @see com.terturl.MMO.Abilities.Ability.AbilityCosts
	 */
	public void addCost(AbilityCosts ac, Double Amount) {
		costs.put(ac, Amount);
	}

	/**
	 * Fires all Effects and will eventually check if the player has the required
	 * costs before firing
	 * 
	 * @param p Player that will use the ability
	 */
//	public void useAbility(Player p) {
//		getEffects().stream().forEach(e -> {
//			EffectInformation ei = e.getEffectInformation();
//			ei.setPlayer(p);
//			if (ei.getLocType().equals(LocationType.PLAYER)) {
//				ei.setLoc(p.getLocation().clone());
//			} else if (ei.getLocType().equals(LocationType.POINT)) {
//				Location loc = p.getEyeLocation().clone();
//				Vector inFrontOf = loc.getDirection().normalize().multiply(ei.getRange());
//				ei.setLoc(loc.add(inFrontOf));
//			} else if (ei.getLocType().equals(LocationType.BLOCKAT)) {
//				ei.setLoc(p.getTargetBlock((Set<Material>) null, 10).getLocation().add(0.5, 0.5, 0.5).clone());
//			}
//			e.setEffectInformation(ei);
//			((Effect) e.clone()).run(p);
//		});
//	}
	
	/**
	 * Fires all Effects and will eventually check if the player has the required
	 * costs before firing
	 * 
	 * @param p Player that will use the ability
	 */
	public void useAbility(Player p) {
		getEffects().stream().forEach(e -> {
			e.getSounds().stream().forEach(s -> {
				p.playSound(p.getLocation(), s.getSound(), s.getVolume(), s.getPitch());
			});
			((Effect) e.clone()).run(p);
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

	/**
	 * Adds any effect to the list of Effects the Ability will fire when used
	 * 
	 * @param e Effect to add
	 * @see com.terturl.MMO.Effects.Effect
	 */
	public void addEffect(Effect e) {
		effects.add(e);
	}

	public enum AbilityCosts {
		HEALTH, MANA, MONEY;
	}

}