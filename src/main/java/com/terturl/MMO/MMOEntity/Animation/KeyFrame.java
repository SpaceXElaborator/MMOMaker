package com.terturl.MMO.MMOEntity.Animation;

import java.math.BigDecimal;
import java.util.UUID;

import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Rotation;

import lombok.Getter;
import lombok.Setter;

/**
 * Java representation of a KeyFrame that currently only holds rotation.
 * Position is in the works
 * 
 * @author Sean Rahman
 * @since 0.61.0
 *
 */
public class KeyFrame {

	@Getter
	@Setter
	private UUID uuid;

	@Getter
	@Setter
	private String partName;

	@Getter
	@Setter
	private BigDecimal time;

	@Getter
	@Setter
	private Rotation rot;

}