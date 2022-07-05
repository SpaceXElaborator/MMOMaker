package com.terturl.MMO.Effects.PlayerEffects;

import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.terturl.MMO.Effects.Effect;

import lombok.Getter;
import lombok.Setter;

public class HealEffect extends Effect {
	
	@Getter @Setter
	private double amount;
	
	public void run(Player p) {
		p.setHealth(p.getHealth() + getAmount());
	}

	public void load(JsonObject jo) {
		amount = jo.get("Amount").getAsDouble();
	}

}