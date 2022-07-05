package com.terturl.MMO.Effects.TimeBasedEffects;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class LimitEffect extends Effect {

	private Effect e;

	@Getter
	private long every;
	
	@Getter
	private int limitTimes;
	
	@Getter
	private long delay;
	
	public void run(Player p) {
		BukkitRunnable br = new BukkitRunnable() {
			private int limit = getLimitTimes();

			public void run() {
				if (limit == 0) {
					cancel();
					return;
				}
				e.run(p);
				limit--;
			}
		};
		br.runTaskTimer(MinecraftMMO.getInstance(), getDelay(),
				getEvery());
	}

	public void load(JsonObject jo) {
		every = jo.get("Every").getAsLong();
		limitTimes = jo.get("Limit").getAsInt();
		delay = jo.get("Delay").getAsLong();
	}
	
}