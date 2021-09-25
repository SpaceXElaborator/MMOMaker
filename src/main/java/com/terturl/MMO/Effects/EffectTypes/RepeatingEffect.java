package com.terturl.MMO.Effects.EffectTypes;

import org.bukkit.scheduler.BukkitRunnable;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Effects.Util.EffectInformation;

import lombok.Getter;
import lombok.Setter;

public class RepeatingEffect extends Effect {

	@Getter @Setter
	private Effect effect;
	
	public RepeatingEffect(EffectInformation ei, Effect e) {
		super(ei);
		effect = e;
	}

	@Override
	public void run() {
		BukkitRunnable br = new BukkitRunnable() {
			private long time = 0;
			private long dur = 0;
			public void run() {
				if(time == getEffectInformation().getEvery()) {
					getEffect().run();
					time = 0;
				}
				time++;
				dur++;
				if(dur == getEffectInformation().getDuration()) {
					cancel();
				}
			}
		};
		br.runTaskTimer(MinecraftMMO.getInstance(), getEffectInformation().getDelay(), 1);
	}
	
}