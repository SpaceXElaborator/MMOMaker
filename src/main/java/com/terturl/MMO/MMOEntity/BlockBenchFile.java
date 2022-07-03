package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.terturl.MMO.MMOEntity.Animation.Animation;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOTexture;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A BlockBenchFile is just a Java representation of the information gathered
 * from a .bbmodel file imported in to use files object placements and rotations
 * to be used in game
 * 
 * @author Sean Rahman
 * @since 0.55.0
 *
 */
@ToString
public class BlockBenchFile {

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private int width;

	@Getter
	@Setter
	private int height;

	@Getter
	@Setter
	private double eyeHeight;

	@Getter
	private List<BBOCube> elements = new ArrayList<>();

	@Getter
	private List<BBOOutliner> outliner = new ArrayList<>();

	@Getter
	private List<BBOTexture> textures = new ArrayList<>();

	@Getter
	private List<Animation> animations = new ArrayList<>();

	/**
	 * Find a BBOCube from the list inside of elements
	 * @param uuid UUID of the block
	 * @return BBOCube object
	 * @see com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube
	 */
	public BBOCube findCubeByUUID(UUID uuid) {
		return elements.stream().filter(e -> e.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	/**
	 * Find a BBOUtliner from the list inside of outliner
	 * @param uuid UUID of the BBOOutlienr
	 * @return BBOOutliner object
	 * @see com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner
	 */
	public BBOOutliner findOutlinerByUUID(UUID uuid) {
		return outliner.stream().filter(e -> e.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	/**
	 * Checks for if the UUID provided is an Outliner or not
	 * @param uuid UUID of the element
	 * @return If the uuid provided is an outliner uuid
	 */
	public boolean isOutliner(UUID uuid) {
		for (BBOOutliner outliner : outliner) {
			if (outliner.getUuid().equals(uuid))
				return true;
		}
		return false;
	}

}