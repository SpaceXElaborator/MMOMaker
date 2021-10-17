package com.terturl.MMO.MMOEntity;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOFace;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner;
import com.terturl.MMO.Util.JsonFileInterpretter;

import net.md_5.bungee.api.ChatColor;

public class MobFileReader {

	public MobFileReader() {
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
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}
	
	private void generateMob(File f) {
		JsonFileInterpretter mobFile = new JsonFileInterpretter(f);
		BlockBenchFile bbf = new BlockBenchFile();
		if(!mobFile.contains("name")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to locate name of mob");
			return;
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
				addOutliners(null, element, bbf);
			}
		}
		
	}
	
	private void addOutliners(String parent, JSONObject jo, BlockBenchFile bbf) {
		JsonFileInterpretter element = new JsonFileInterpretter(jo);
		BBOOutliner outliner = new BBOOutliner();
		outliner.setName(element.getString("name"));
		outliner.setUuid(UUID.fromString(element.getString("uuid")));
		
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
					addOutliners(outliner.getName(), jo2, bbf);
					continue;
				}
				String uuid = o.toString();
				outliner.getChildren().add(UUID.fromString(uuid));
			}
		}
		
		bbf.getOutliner().add(outliner);
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
	
}