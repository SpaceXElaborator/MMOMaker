package com.terturl.MMO.MMOEntity.ResourcePack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.MMOEntity.BlockBenchFile;
import com.terturl.MMO.MMOEntity.MMOMobEntity;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOTexture;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.RPItem;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.RPPredicate;
import com.terturl.MMO.Util.JSONHelpers.ResourcePackJSON;

import lombok.Getter;

/**
 * Will create the information needed to create a resource pack that will be
 * able to display the newly created MMOMob to all players on the server, if
 * they do not have this resource pack, the MMOMob will be a collection of
 * saddles on their screen
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
public class ResourcePackGenerator {

	@Getter
	private File itemModels;

	@Getter
	private File mobModels;

	@Getter
	private File mobTextures;

	@Getter
	private RPItem item;

	/**
	 * Will create all the necessary folders needed for the creation of the resource
	 * pack as well as create the new RPItem that will contain all information about
	 * the CustomItemModel and location of mobel for that CustomItemModel
	 * https://www.planetminecraft.com/forums/communities/texturing/new-1-14-custom-item-models-tuto-578834/
	 */
	public ResourcePackGenerator() {
		File f = new File(MinecraftMMO.getInstance().getDataFolder(), "resource-pack");
		if (f.exists())
			f.delete();
		f.mkdir();

		File packMeta = new File(f, "pack.mcmeta");
		writePackInformation(packMeta);

		itemModels = new File(f, "assets/minecraft/models/item");
		itemModels.mkdirs();

		mobModels = new File(f, "assets/mmorpg/models");
		mobModels.mkdirs();

		mobTextures = new File(f, "assets/mmorpg/textures/entity");
		mobTextures.mkdirs();

		item = new RPItem();
	}

	/**
	 * Will turn the BlockBenchFile into a newly created MMOMobEntity that contains
	 * the information for creating a MMOMob
	 * 
	 * @param bbf BlockBenchFile to convert into a MMOMobEntity
	 * @see com.terturl.MMO.MMOEntity.MMOMobEntity
	 */
	public void addMob(BlockBenchFile bbf) {
		MMOMobEntity mob = new MMOMobEntity(bbf.getName(), bbf);
		File f = new File(mobModels, mob.getBbf().getName().toLowerCase());
		f.mkdir();

		for (BBOTexture tex : bbf.getTextures()) {
			writeTextureFile(tex);
		}

		for (MobBoneFile mbf : mob.getBoneFiles()) {
			generateMobFile(mbf, f);
		}
		updateTexturePack(mob);
		MinecraftMMO.getInstance().getMobManager().registerMobEntity(mob);
	}

	private void writeTextureFile(BBOTexture tex) {
		File f = new File(mobTextures, tex.getName().toLowerCase() + ".png");
		try {
			ImageIO.write(tex.getTexture(), "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void generateMobFile(MobBoneFile mbf, File parent) {
		File f = new File(parent, mbf.getFile().toLowerCase() + ".json");
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		JSONObject textures = new JSONObject();
		mbf.getTextures().forEach((k, v) -> {
			textures.put(k, v);
		});
		treeMap.put("textures", textures);

		JSONArray elements = new JSONArray();
		mbf.getElements().forEach(e -> {
			elements.add(ResourcePackJSON.cubeToJSON(e));
		});
		treeMap.put("elements", elements);

		JSONObject display = new JSONObject();
		mbf.getDisplay().forEach((k, v) -> {
			display.put(k, ResourcePackJSON.displayToJSON(v));
		});
		treeMap.put("display", display);

		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = g.toJson(treeMap);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(prettyJson);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Takes a MMOMobEntity and will add all of it's parts into the RPItem and set
	 * its texture mapping to be used in MMOMob
	 * 
	 * @param mfr
	 */
	public void updateTexturePack(MMOMobEntity mfr) {
		Map<String, Integer> partTextureId = new HashMap<>();
		mfr.getBones().forEach((k, v) -> {
			item.addOverride(mfr.getBbf().getName(), k);
			partTextureId.put(k.toLowerCase(), item.getData());
		});
		mfr.setTextureMapping(partTextureId);
	}

	/**
	 * Creates the saddle.json file with the needed overrides and predicates for the
	 * CustomItemModel to be read correctly by Minecraft for presenting to the
	 * player in game
	 */
	@SuppressWarnings("unchecked")
	public void createItemJSONFile() {
		File f = new File(itemModels, "saddle.json");
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		treeMap.put("data", item.getData());
		treeMap.put("parent", item.getParent());
		JSONObject textures = new JSONObject();
		item.getTextures().forEach((k, v) -> {
			textures.put(k, v);
		});
		treeMap.put("textures", textures);
		JSONArray overrides = new JSONArray();
		for (RPPredicate predicate : item.getOverrides()) {
			JSONObject override = new JSONObject();
			JSONObject cmd = new JSONObject();
			cmd.put("custom_model_data", predicate.getCustomModelData());
			override.put("predicate", cmd);
			override.put("model", predicate.getModel());
			overrides.add(override);
		}
		treeMap.put("overrides", overrides);
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = g.toJson(treeMap);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(prettyJson);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void writePackInformation(File f) {
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		JSONObject pack = new JSONObject();
		pack.put("description", "");
		pack.put("pack_format", 7);
		treeMap.put("pack", pack);
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = g.toJson(treeMap);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(prettyJson);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}