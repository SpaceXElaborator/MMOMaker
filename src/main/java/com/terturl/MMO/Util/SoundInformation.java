package com.terturl.MMO.Util;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

public class SoundInformation {

	@Getter @Setter
	private Location loc;
	@Getter @Setter
	private String sound;
	@Getter @Setter
	private float volume;
	@Getter @Setter
	private float pitch;
	
}