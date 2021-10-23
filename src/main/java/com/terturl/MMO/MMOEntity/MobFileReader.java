package com.terturl.MMO.MMOEntity;

import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

public class MobFileReader {

	private ResourcePackGenerator rpg;
	
	public MobFileReader() {
		rpg = new ResourcePackGenerator();
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Mobs...");
		File f = new File(MinecraftMMO.getInstance().getDataFolder(), "mmo-entity");
		if(!f.exists()) f.mkdir();
		File[] files = f.listFiles();
		if(files.length > 0) {
			for(File mob : files) {
				if(mob.getName().endsWith(".json") || mob.getName().endsWith(".bbmodel")) {
					generateMob(mob);
				}
			}
		}
		rpg.createItemJSONFile();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}
	
	private void generateMob(File f) {
		JsonFileInterpretter mobFile = new JsonFileInterpretter(f);
		BlockBenchFile bbf = new BlockBenchFile();
		if(!mobFile.contains("name")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to locate name of mob");
			return;
		}
		bbf.setName(mobFile.getString("name").toLowerCase());
		
		if(mobFile.contains("resolution")) {
			JsonFileInterpretter element = new JsonFileInterpretter(mobFile.getObject("resolution"));
			bbf.setHeight(element.getInt("height"));
			bbf.setWidth(element.getInt("width"));
		}
		
		if(mobFile.contains("elements")) {
			JSONArray ja = mobFile.getArray("elements");
			for(Object elements : ja) {
				JSONObject element = (JSONObject)elements;
				BBOCube cube = createCube(element);
				bbf.getElements().add(cube);
			}
		}
		
		if(mobFile.contains("outliner")) {
			JSONArray ja = mobFile.getArray("outliner");
			for(Object elements : ja) {
				JSONObject element = (JSONObject)elements;
				BBOOutliner outliner = addOutliners(null, element, bbf);
				bbf.getOutliner().add(outliner);
			}
		}
		
		if(mobFile.contains("textures")) {
			JSONArray ja = mobFile.getArray("textures");
			for(Object elements : ja) {
				JSONObject element = (JSONObject)elements;
				bbf.getTextures().add(createTexture(element));
			}
		}
		
		if(mobFile.contains("animations")) {
			JSONArray anims = mobFile.getArray("animations");
			for(Object elements : anims) {
				JSONObject animation = (JSONObject)elements;
				Animation anim = createAnimation(animation);
				bbf.getAnimations().add(anim);
			}
		}
		
		rpg.addMob(bbf);
	}
	
	private BBOTexture createTexture(JSONObject jo) {
		JsonFileInterpretter element = new JsonFileInterpretter(jo);
		BBOTexture texture = new BBOTexture();
		texture.setName(element.getString("name"));
		texture.setUuid(UUID.fromString(element.getString("uuid")));
		texture.setTexture(ImageCreator.fromString(element.getString("source")));
		texture.setId(element.getInt("id"));
		return texture;
	}
	
	private BBOOutliner addOutliners(String parent, JSONObject jo, BlockBenchFile bbf) {
		JsonFileInterpretter element = new JsonFileInterpretter(jo);
		BBOOutliner outliner = new BBOOutliner();
		
		outliner.setName(element.getString("name"));
		outliner.setUuid(UUID.fromString(element.getString("uuid")));
		outliner.setParent(parent);
		
		if(element.contains("origin")) {
			JSONArray origin = element.getArray("origin");
			outliner.setOrigin(Double.valueOf(origin.get(0).toString()), Double.valueOf(origin.get(1).toString()), Double.valueOf(origin.get(2).toString()));
		}
		
		if(element.contains("rotation")) {
			JSONArray rotation = element.getArray("rotation");
			outliner.setRotation(Double.valueOf(rotation.get(0).toString()), Double.valueOf(rotation.get(1).toString()), Double.valueOf(rotation.get(2).toString()));
		}
		
		if(element.contains("children")) {
			JSONArray children = element.getArray("children");
			for(Object o : children) {
				if(o instanceof JSONObject) {
					JSONObject jo2 = (JSONObject)o;
					BBOOutliner outliner2 = addOutliners(outliner.getName(), jo2, bbf);
					bbf.getOutliner().add(outliner2);
					outliner.getChildren().add(outliner2.getUuid());
					continue;
				}
				String uuid = o.toString();
				outliner.getChildren().add(UUID.fromString(uuid));
			}
		}
		
		return outliner;
	}
	
	private BBOCube createCube(JSONObject jo) {
		JsonFileInterpretter element = new JsonFileInterpretter(jo);
		BBOCube cube = new BBOCube();
		cube.setName(element.getString("name"));
		JSONArray to = element.getArray("to");
		cube.setTo(Double.parseDouble(to.get(0).toString()), Double.parseDouble(to.get(1).toString()), Double.parseDouble(to.get(2).toString()));
		JSONArray from = element.getArray("from");
		cube.setFrom(Double.parseDouble(from.get(0).toString()), Double.parseDouble(from.get(1).toString()), Double.parseDouble(from.get(2).toString()));
		if(element.contains("origin")) {
			JSONArray origin = element.getArray("origin");
			cube.setOrigin(Double.parseDouble(origin.get(0).toString()), Double.parseDouble(origin.get(1).toString()), Double.parseDouble(origin.get(2).toString()));
		}
		if(element.contains("rotation")) {
			JSONArray rotation = element.getArray("rotation");
			cube.setRotation(Double.parseDouble(rotation.get(0).toString()), Double.parseDouble(rotation.get(1).toString()), Double.parseDouble(rotation.get(2).toString()));
		}
		
		JSONObject facesObject = element.getObject("faces");
		JsonFileInterpretter faces = new JsonFileInterpretter(facesObject);
		
		cube.getFaces().put("north", createFace(faces.getObject("north")));
		cube.getFaces().put("east", createFace(faces.getObject("east")));
		cube.getFaces().put("south", createFace(faces.getObject("south")));
		cube.getFaces().put("west", createFace(faces.getObject("west")));
		cube.getFaces().put("up", createFace(faces.getObject("up")));
		cube.getFaces().put("down", createFace(faces.getObject("down")));
		
		cube.setUuid(UUID.fromString(element.getString("uuid")));
		
		return cube;
	}
	
	private BBOFace createFace(JSONObject jo) {
		BBOFace face = new BBOFace();
		JsonFileInterpretter f = new JsonFileInterpretter(jo);
		JSONArray uv = f.getArray("uv");
		face.setUV(Double.parseDouble(uv.get(0).toString()), Double.parseDouble(uv.get(1).toString()), Double.parseDouble(uv.get(2).toString()), Double.parseDouble(uv.get(3).toString()));
		face.setTexture((f.get("texture") == null) ? null : f.getInt("texture"));
		return face;
	}
	
	private Animation createAnimation(JSONObject jo) {
		Animation anim = new Animation();
		JsonFileInterpretter f = new JsonFileInterpretter(jo);
		anim.setUuid(UUID.fromString(f.getString("uuid")));
		anim.setName(f.getString("name").replaceAll("animation.model.", "").toLowerCase());
		anim.setLoop((f.getString("loop").equalsIgnoreCase("loop")) ? true : false);
		anim.setIsOverride(f.getBoolean("override"));
		anim.setLength(new BigDecimal(f.getString("length")));
		
		if(f.contains("animators")) {
			JSONObject animators = f.getObject("animators");
			for(Object animator : animators.keySet()) {
				String s = animator.toString();
				JSONObject animatorInformation = (JSONObject) animators.get(s);
				Animator an = createAnimator(s, animatorInformation);
				anim.getFrames().add(an);
			}
		}
		
		return anim;
	}
	
	private Animator createAnimator(String uuid, JSONObject jo) {
		Animator anim = new Animator();
		JsonFileInterpretter f = new JsonFileInterpretter(jo);
		anim.setPartId(UUID.fromString(uuid));
		
		JSONArray keyframes = f.getArray("keyframes");
		for(Object o : keyframes) {
			JSONObject keyframe = (JSONObject)o;
			KeyFrame frame = createKeyFrame(f.getString("name").toLowerCase(), keyframe);
			anim.getKeyFrames().add(frame);
		}
		
		return anim;
	}
	
	private KeyFrame createKeyFrame(String s, JSONObject jo) {
		KeyFrame frame = new KeyFrame();
		JsonFileInterpretter f = new JsonFileInterpretter(jo);
		
		frame.setPartName(s);
		String channel = f.getString("channel");
		if(!channel.equalsIgnoreCase("rotation")) return null;
		frame.setTime(new BigDecimal(f.getString("time")));
		frame.setUuid(UUID.fromString(f.getString("uuid")));
		JSONArray dataPoints = f.getArray("data_points");
		JSONObject dataPoint = (JSONObject) dataPoints.get(0);
		JsonFileInterpretter dataPointF = new JsonFileInterpretter(dataPoint);
		Rotation rot = new Rotation();
		rot.setOrigin(dataPointF.getDouble("x"), dataPointF.getDouble("y"), dataPointF.getDouble("z"));
		frame.setRot(rot);
		
		return frame;
	}
	
}