package com.terturl.MMO.Effects.TimeBasedEffects;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class RepeatingEffect extends Effect {

	private Effect e;

	@Getter
	private double every;
	
	@Getter
	private double duration;
	
	@Getter
	private long delay;
	
	public void run(Player p) {
		BukkitRunnable br = new BukkitRunnable() {
			private long time = 0;
			private long dur = 0;

			public void run() {
				if (time == getEvery()) {
					e.run(p);
					time = 0;
				}
				time++;
				dur++;
				if (dur == getDuration()) {
					cancel();
				}
			}
		};
		br.runTaskTimer(MinecraftMMO.getInstance(), getDelay(), 1);
	}

	public void load(JsonObject jo) {
		every = jo.get("Every").getAsDouble();
		duration = jo.get("Duration").getAsDouble();
		delay = jo.get("Delay").getAsLong();
	}
	
}