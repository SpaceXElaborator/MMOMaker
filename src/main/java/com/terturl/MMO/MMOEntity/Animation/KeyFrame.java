package com.terturl.MMO.MMOEntity.Animation;

import java.math.BigDecimal;
import java.util.UUID;

import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Rotation;

import lombok.Getter;
import lombok.Setter;

public class KeyFrame {

	@Getter @Setter
	private UUID uuid;
	
	@Getter @Setter
	private String partName;
	
	@Getter @Setter
	private BigDecimal time;
	
	@Getter @Setter
	private Rotation rot;
	
}