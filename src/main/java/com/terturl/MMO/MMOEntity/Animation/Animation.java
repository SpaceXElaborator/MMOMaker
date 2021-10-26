package com.terturl.MMO.MMOEntity.Animation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Java representation of BlockBenches Animation taken from the .bbmodel/.json
 * file
 * 
 * @author Sean Rahman
 * @since 0.61.0
 *
 */
public class Animation {

	@Getter
	@Setter
	private UUID uuid;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private Boolean isOverride;

	@Getter
	@Setter
	private BigDecimal length;

	@Getter
	@Setter
	private Boolean loop;

	@Getter
	private List<Animator> frames = new ArrayList<>();

}