package com.terturl.MMO.MMOEntity.Animation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Animator {
	
	@Getter @Setter
	private UUID partId;
	
	@Getter
	private List<KeyFrame> keyFrames = new ArrayList<>();
	
}