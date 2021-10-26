package com.terturl.MMO.Util;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds the name of the sound, location to play the sound, the volume of the
 * sound, and the pitch of the sound to be used when playing sounds
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class SoundInformation {

	@Getter
	@Setter
	private Location loc;
	@Getter
	@Setter
	private String sound;
	@Getter
	@Setter
	private float volume;
	@Getter
	@Setter
	private float pitch;

}