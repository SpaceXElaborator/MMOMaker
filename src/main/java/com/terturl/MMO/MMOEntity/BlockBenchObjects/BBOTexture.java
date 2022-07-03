package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import java.awt.image.BufferedImage;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A texture representation class that holds an integer value for determination
 * of the texture as well as the actual BufferedImage obtained from the Base64
 * string to be used in game for presenting the item
 * 
 * @author Sean Rahman
 * @since 0.57.0
 *
 */
@ToString
public class BBOTexture {

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private int id;

	@Getter
	@Setter
	private UUID uuid;

	@Getter
	@Setter
	private BufferedImage texture;

}