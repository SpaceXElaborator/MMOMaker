package com.terturl.MMO.Effects.TimeBasedEffects;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

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

	public void load(JSONObject jo) {
		every = Long.parseLong(jo.get("Every").toString());
		limitTimes = Integer.parseInt(jo.get("Limit").toString());
		delay = Long.parseLong(jo.get("Delay").toString());
	}
	
}