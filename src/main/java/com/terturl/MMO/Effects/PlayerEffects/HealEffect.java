package com.terturl.MMO.Effects.PlayerEffects;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.terturl.MMO.Effects.Effect;

import lombok.Getter;
import lombok.Setter;

public class HealEffect extends Effect {
	
	@Getter @Setter
	private Double amount;
	
	public void run(Player p) {
		p.setHealth(p.getHealth() + getAmount());
	}

	public void load(JSONObject jo) {
		amount = Double.parseDouble(jo.get("Amount").toString());
	}

}