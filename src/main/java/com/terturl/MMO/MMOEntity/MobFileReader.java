package com.terturl.MMO.MMOEntity;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.MMOEntity.Animation.Animation;
import com.terturl.MMO.MMOEntity.Animation.Animator;
import com.terturl.MMO.MMOEntity.Animation.KeyFrame;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOFace;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOTexture;
import com.terturl.MMO.MMOEntity.ResourcePack.ResourcePackGenerator;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Rotation;
import com.terturl.MMO.Util.ImageCreator;
import com.terturl.MMO.Util.JsonFileInterpretter;

import net.md_5.bungee.api.ChatColor;

/**
 * Will create and generate MMOMobEntity files and call the
 * ResourcePackGenerator to start generating the resource pack to visually see
 * the mob in Spigot
 * 
 * @author Sean Rahman
 * @since 0.55.0
 *
 */
public class MobFileReader {

	private ResourcePackGenerator rpg;

	/**
	 * Reads all .json or .bbmodel files from the /plugins/MinecraftMMO/mmo-entity
	 * folder
	 */
	public MobFileReader() {
		rpg = new ResourcePackGenerator();
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Mobs...");
		File f = new File(MinecraftMMO.getInstance().getDataFolder(), "mmo-entity");
		if (!f.exists())
			f.mkdir();
		File[] files = f.listFiles();
		if (files.length > 0) {
			for (File mob : files) {
				if (mob.getName().endsWith(".json") || mob.getName().endsWith(".bbmodel")) {
					generateMob(mob);
				}
			}
		}

		// Will update the Item JSON file to contain information about all mobs that
		// have been added
		rpg.createItemJSONFile();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}

	private void generateMob(File f) {
		JsonObject mobFile = new JsonFileInterpretter(f).getJson();
		BlockBenchFile bbf = new BlockBenchFile();
		if (!mobFile.has("name")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to locate name of mob");
			return;
		}
		bbf.setName(mobFile.get("name").getAsString().toLowerCase());

		if (mobFile.has("resolution") && mobFile.get("resolution").isJsonObject()) {
			JsonObject element = mobFile.get("resolution").getAsJsonObject();
			bbf.setHeight(element.get("height").getAsInt());
			bbf.setWidth(element.get("width").getAsInt());
		}

		if (mobFile.has("elements") && mobFile.get("elements").isJsonArray()) {
			JsonArray ja = mobFile.get("elements").getAsJsonArray();
			for (JsonElement elements : ja) {
				if(!elements.isJsonObject()) continue;
				JsonObject element = elements.getAsJsonObject();
				BBOCube cube = createCube(element);
				bbf.getElements().add(cube);
			}
		}

		if (mobFile.has("outliner") && mobFile.get("outliner").isJsonArray()) {
			JsonArray ja = mobFile.get("outliner").getAsJsonArray();
			for (JsonElement elements : ja) {
				if(!elements.isJsonObject()) continue;
				JsonObject element = elements.getAsJsonObject();
				BBOOutliner outliner = addOutliners(null, element, bbf);
				bbf.getOutliner().add(outliner);
			}
		}

		if (mobFile.has("textures") && mobFile.get("textures").isJsonArray()) {
			JsonArray ja = mobFile.get("textures").getAsJsonArray();
			for (JsonElement elements : ja) {
				if(!elements.isJsonObject()) continue;
				JsonObject element = elements.getAsJsonObject();
				bbf.getTextures().add(createTexture(element));
			}
		}

		if (mobFile.has("animations") && mobFile.get("animations").isJsonArray()) {
			JsonArray ja = mobFile.get("animations").getAsJsonArray();
			for (JsonElement elements : ja) {
				if(!elements.isJsonObject()) continue;
				JsonObject animation = elements.getAsJsonObject();
				Animation anim = createAnimation(animation);
				bbf.getAnimations().add(anim);
			}
		}

		rpg.addMob(bbf);
	}

	private BBOTexture createTexture(JsonObject element) {
		BBOTexture texture = new BBOTexture();
		texture.setName(element.get("name").getAsString());
		texture.setUuid(UUID.fromString(element.get("uuid").getAsString()));
		texture.setTexture(ImageCreator.fromString(element.get("source").getAsString()));
		texture.setId(element.get("id").getAsInt());
		return texture;
	}

	private BBOOutliner addOutliners(String parent, JsonObject element, BlockBenchFile bbf) {
		BBOOutliner outliner = new BBOOutliner();

		outliner.setName(element.get("name").getAsString());
		outliner.setUuid(UUID.fromString(element.get("uuid").getAsString()));
		outliner.setParent(parent);

		if (element.has("origin") && element.get("origin").isJsonArray()) {
			JsonArray array = element.get("origin").getAsJsonArray();
			outliner.setOrigin(array.get(0).getAsDouble(), array.get(1).getAsDouble(),
					array.get(2).getAsDouble());
		}

		if (element.has("rotation") && element.get("rotation").isJsonArray()) {
			JsonArray array = element.get("rotation").getAsJsonArray();
			outliner.setOrigin(array.get(0).getAsDouble(), array.get(1).getAsDouble(),
					array.get(2).getAsDouble());
		}

		// Sometimes the children are just UUID strings. This block of cude will
		// determine if a new outliner needs to be created and added to the parent
		// outliner or if it will just add the UUID of the cube
		if(element.has("children") && element.get("children").isJsonArray()) {
			JsonArray array = element.get("children").getAsJsonArray();
			for(JsonElement je : array) {
				if(je.isJsonObject()) {
					JsonObject jo = je.getAsJsonObject();
					BBOOutliner outliner2 = addOutliners(outliner.getName(), jo, bbf);
					bbf.getOutliner().add(outliner2);
					outliner.getChildren().add(outliner2.getUuid());
				} else {
					String uuid = je.getAsString();
					outliner.getChildren().add(UUID.fromString(uuid));
				}
			}
		}

		return outliner;
	}

	private BBOCube createCube(JsonObject element) {
		BBOCube cube = new BBOCube();
		
		cube.setName(element.get("name").getAsString());
		
		JsonArray to = element.get("to").getAsJsonArray();
		cube.setTo(to.get(0).getAsDouble(),to.get(1).getAsDouble(),
				to.get(2).getAsDouble());
		
		JsonArray from = element.get("from").getAsJsonArray();
		cube.setFrom(from.get(0).getAsDouble(),from.get(1).getAsDouble(),
				from.get(2).getAsDouble());
		
		if (element.has("origin") && element.get("origin").isJsonArray()) {
			JsonArray origin = element.get("origin").getAsJsonArray();
			cube.setOrigin(origin.get(0).getAsDouble(),origin.get(1).getAsDouble(),
					origin.get(2).getAsDouble());
		}
		
		if (element.has("rotation") && element.get("rotation").isJsonArray()) {
			JsonArray rotation = element.get("rotation").getAsJsonArray();
			cube.setRotation(rotation.get(0).getAsDouble() ,rotation.get(1).getAsDouble(),
					rotation.get(2).getAsDouble());
		}

		JsonObject faces = element.get("faces").getAsJsonObject();

		cube.getFaces().put("north", createFace(faces.get("north").getAsJsonObject()));
		cube.getFaces().put("east", createFace(faces.get("east").getAsJsonObject()));
		cube.getFaces().put("south", createFace(faces.get("south").getAsJsonObject()));
		cube.getFaces().put("west", createFace(faces.get("west").getAsJsonObject()));
		cube.getFaces().put("up", createFace(faces.get("up").getAsJsonObject()));
		cube.getFaces().put("down", createFace(faces.get("down").getAsJsonObject()));

		cube.setUuid(UUID.fromString(element.get("uuid").getAsString()));

		return cube;
	}

	private BBOFace createFace(JsonObject jo) {
		if(!jo.has("uv") || !jo.get("uv").isJsonArray()) return null;
		
		BBOFace face = new BBOFace();
		JsonArray uv = jo.get("uv").getAsJsonArray();
		face.setUV(uv.get(0).getAsDouble(), uv.get(1).getAsDouble(),
				uv.get(2).getAsDouble(), uv.get(3).getAsDouble());
		face.setTexture((jo.get("texture") == null) ? null : jo.get("texture").getAsInt());
		return face;
	}

	private Animation createAnimation(JsonObject jo) {
		Animation anim = new Animation();
		anim.setUuid(UUID.fromString(jo.get("uuid").getAsString()));
		anim.setName(jo.get("name").getAsString().replaceAll("animation.model.", "").toLowerCase());
		anim.setLoop((jo.get("loop").getAsString().equalsIgnoreCase("loop")) ? true : false);
		anim.setOverride(jo.get("override").getAsBoolean());
		anim.setLength(new BigDecimal(jo.get("length").getAsBigInteger()));

		if (jo.has("animators") && jo.get("animators").isJsonObject()) {
			JsonObject animators = jo.get("animators").getAsJsonObject();
			for (Entry<String, JsonElement> animator : animators.entrySet()) {
				if(!animator.getValue().isJsonObject()) continue;
				JsonObject animatorInformation = animator.getValue().getAsJsonObject();
				Animator an = createAnimator(animator.getKey(), animatorInformation);
				anim.getFrames().add(an);
			}
		}

		return anim;
	}

	private Animator createAnimator(String uuid, JsonObject jo) {
		Animator anim = new Animator();
		anim.setPartId(UUID.fromString(uuid));

		if(!jo.has("keyframes")) return null;
		if(!jo.get("keyframes").isJsonArray()) return null;
		
		JsonArray keyframes = jo.get("keyframes").getAsJsonArray();
		for(JsonElement je : keyframes) {
			if(!je.isJsonObject()) continue;
			JsonObject keyframe = je.getAsJsonObject();
			KeyFrame frame = createKeyFrame(jo.get("name").getAsString().toLowerCase(), keyframe);
			anim.getKeyFrames().add(frame);
		}

		return anim;
	}

	private KeyFrame createKeyFrame(String s, JsonObject jo) {
		KeyFrame frame = new KeyFrame();
		frame.setPartName(s);
		String channel = jo.get("channel").getAsString();
		if (!channel.equalsIgnoreCase("rotation"))
			return null;
		frame.setTime(new BigDecimal(jo.get("time").getAsBigInteger()));
		frame.setUuid(UUID.fromString(jo.get("uuid").getAsString()));
		
		if(jo.has("data_points") && jo.get("data_points").isJsonArray()) {
			JsonArray dataPoints = jo.get("data_points").getAsJsonArray();
			JsonObject dataPoint = dataPoints.get(0).getAsJsonObject();
			Rotation rot = new Rotation();
			rot.setOrigin(dataPoint.get("x").getAsDouble(), dataPoint.get("y").getAsDouble(), dataPoint.get("z").getAsDouble());
			frame.setRot(rot);
		}
		
		return frame;
	}

}