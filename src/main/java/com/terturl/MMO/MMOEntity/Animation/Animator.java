package com.terturl.MMO.MMOEntity.Animation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Java representation of a Animator that holds what part is getting animated
 * and the all keyframes associated with that part
 * 
 * @author Sean Rahman
 * @since 0.63.0
 *
 */
public class Animator {

	@Getter
	@Setter
	private UUID partId;

	@Getter
	private List<KeyFrame> keyFrames = new ArrayList<>();

}