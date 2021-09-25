package com.terturl.MMO.Effects.EffectTypes;

import org.bukkit.scheduler.BukkitRunnable;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Effects.Util.EffectInformation;

import lombok.Getter;
import lombok.Setter;

public class LimitEffect extends Effect {

	@Getter @Setter
	private Effect effect;
	
	public LimitEffect(EffectInformation ei, Effect e) {
		super(ei);
		effect = e;
	}

	@Override
	public void run() {
		BukkitRunnable br = new BukkitRunnable() {
			private int limit = getEffectInformation().getLimitTimes();
			public void run() {
				if(limit == 0) {
					cancel();
					return;
				}
				getEffect().run();
				limit--;
			}
		};
		br.runTaskTimer(MinecraftMMO.getInstance(), getEffectInformation().getDelay(), getEffectInformation().getEvery());
	}
	
}