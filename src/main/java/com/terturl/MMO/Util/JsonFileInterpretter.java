package com.terturl.MMO.Util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.ToString;

/**
 * Helper class that make it simple to read JSON values and parse JSON strings
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
@ToString
public class JsonFileInterpretter {

	@Getter
	private JsonObject json;

	/**
	 * Creates a new instance based off of reading a JSON file
	 * 
	 * @param f File to parse
	 */
	public JsonFileInterpretter(File f) {
		Reader reader = null;
		try {
			reader = Files.newBufferedReader(Paths.get(f.getAbsolutePath()));
		} catch (IOException e) {
			// TODO: Handle exception officially
			e.printStackTrace();
		}
		
		if(reader == null) {
			// TODO: Stop and continue
			return;
		}
		
		json = new Gson().fromJson(reader, JsonObject.class);
	}

	/**
	 * Creates a new instance based off of a JSONObject for same functionality
	 * 
	 * @param jo
	 */
	public JsonFileInterpretter(JsonObject jo) {
		json = jo;
	}

//	/**
//	 * Gets the value of an unidentified object
//	 * 
//	 * @param key Key to check
//	 * @return Object
//	 */
//	public Object get(String key) {
//		return json.get(key);
//	}
//
//	/**
//	 * Gets the value of the base JSON
//	 * 
//	 * @return JSONObject
//	 */
//	public JSONObject getBase() {
//		return json;
//	}
//
//	/**
//	 * Checks if Json contains they specified key
//	 * 
//	 * @param key Key to check
//	 * @return Boolean
//	 */
//	public boolean contains(String key) {
//		return json.containsKey(key);
//	}
//
//	/**
//	 * Gets the value of a String object
//	 * 
//	 * @param key Key to check
//	 * @return String
//	 */
//	public String getString(String key) {
//		return json.get(key).toString();
//	}
//
//	/**
//	 * Gets the value of a Boolean object
//	 * 
//	 * @param key Key to check
//	 * @return Boolean
//	 */
//	public boolean getBoolean(String key) {
//		return Boolean.valueOf(json.get(key).toString());
//	}
//
//	/**
//	 * Gets the value of a Double object
//	 * 
//	 * @param key Key to check
//	 * @return Double
//	 */
//	public double getDouble(String key) {
//		return Double.parseDouble(json.get(key).toString());
//	}
//
//	/**
//	 * Gets the value of a Float object
//	 * 
//	 * @param key Key to check
//	 * @return Float
//	 */
//	public float getFloat(String key) {
//		return Float.parseFloat(json.get(key).toString());
//	}
//
//	/**
//	 * Gets the value of a Integer object
//	 * 
//	 * @param key Key to check
//	 * @return Integer
//	 */
//	public int getInt(String key) {
//		return Integer.parseInt(json.get(key).toString());
//	}
//
//	/**
//	 * Gets the value of a JSONObject object
//	 * 
//	 * @param key Key to check
//	 * @return JSONObject
//	 */
//	public JSONObject getObject(String key) {
//		return (JSONObject) json.get(key);
//	}
//
//	/**
//	 * Gets the value of a String list object
//	 * 
//	 * @param key Key to check
//	 * @return List<String>
//	 */
//	public List<String> getStringList(String key) {
//		List<String> lore = new ArrayList<>();
//		JSONArray array = getArray(key);
//		for (int i = 0; i < array.size(); i++) {
//			lore.add(array.get(i).toString());
//		}
//		return lore;
//	}
//
//	/**
//	 * Gets the value of a JSONArray object
//	 * 
//	 * @param key Key to check
//	 * @return JSONArray
//	 */
//	public JSONArray getArray(String key) {
//		return (JSONArray) json.get(key);
//	}

}