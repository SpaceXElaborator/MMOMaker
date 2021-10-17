package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.awt.image.BufferedImage;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BBOTexture {

	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private Integer id;
	
	@Getter @Setter
	private UUID uuid;
	
	@Getter @Setter
	private BufferedImage texture;
	
}