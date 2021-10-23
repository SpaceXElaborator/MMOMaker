package com.terturl.MMO.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.terturl.MMO.MinecraftMMO;

import lombok.ToString;

@ToString
public class JsonFileInterpretter {
	
	private JSONObject json;
	private JSONParser parser = new JSONParser();
	
	public JsonFileInterpretter(File f) {
		try {
			json = (JSONObject)parser.parse(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		} catch (IOException | ParseException e) {
			MinecraftMMO.getInstance().getLogger().log(Level.INFO, f.getName());
			e.printStackTrace();
		}
	}
	
	public JsonFileInterpretter(JSONObject jo) {
		json = jo;
	}
	
	public Object get(String key) {
		return json.get(key);
	}
	
	public JSONObject getBase() {
		return json;
	}
	
	public Boolean contains(String key) {
		return json.containsKey(key);
	}
	
	public String getString(String key) {
		return json.get(key).toString();
	}
	
	public Boolean getBoolean(String key) {
		return Boolean.valueOf(json.get(key).toString());
	}
	
	public Double getDouble(String key) {
		return Double.parseDouble(json.get(key).toString());
	}
	
	public Float getFloat(String key) {
		return Float.parseFloat(json.get(key).toString());
	}
	
	public Integer getInt(String key) {
		return Integer.parseInt(json.get(key).toString());
	}
	
	public JSONObject getObject(String key) {
		return (JSONObject)json.get(key);
	}
	
	public List<String> getStringList(String key) {
		List<String> lore = new ArrayList<>();
		JSONArray array = getArray(key);
		for(int i = 0; i < array.size(); i++) {
			lore.add(array.get(i).toString());
		}
		return lore;
	}
	
	public JSONArray getArray(String key) {
		return (JSONArray) json.get(key);
	}
	
}