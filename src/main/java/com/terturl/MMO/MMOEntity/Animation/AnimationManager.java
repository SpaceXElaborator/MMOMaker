package com.terturl.MMO.MMOEntity.Animation;

import java.math.BigDecimal;

import org.bukkit.util.EulerAngle;

import com.terturl.MMO.MMOEntity.MMOMob;
import com.terturl.MMO.MMOEntity.ArmorStand.ArmorStandPart;

import lombok.Getter;

public class AnimationManager {
	
	private Animation animation;
	private MMOMob mob;
	private BigDecimal time = new BigDecimal("0.0");
	private BigDecimal length;
	
	@Getter
	private Boolean finished = false;
	
	public AnimationManager(MMOMob m, Animation anim) {
		animation = anim;
		mob = m;
		length = anim.getLength();
	}

	public void tick() {
		for(Animator an : animation.getFrames()) {
			for(KeyFrame frame : an.getKeyFrames()) {
				if(time.compareTo(frame.getTime()) == 0) {
					ArmorStandPart part = mob.getArmorStandPart(frame.getPartName());
					part.setPartRotate(new EulerAngle(frame.getRot().getOrigin()[0], frame.getRot().getOrigin()[1], frame.getRot().getOrigin()[2]));
				}
			}
		}
		time.add(new BigDecimal("0.1"));
		if(time.compareTo(length) > 0) {
			if(animation.getLoop()) time = new BigDecimal("0.0");
			else finished = true;
		}
	}
	
}