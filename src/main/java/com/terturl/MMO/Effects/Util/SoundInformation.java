package com.terturl.MMO.Effects.Util;

import org.bukkit.Location;
import org.bukkit.Sound;

import lombok.Getter;
import lombok.Setter;

public class SoundInformation {

	@Getter @Setter
	private Location loc;
	@Getter @Setter
	private Sound sound;
	@Getter @Setter
	private float volume;
	@Getter @Setter
	private float pitch;
	
}