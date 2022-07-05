package com.terturl.MMO.Util.JSONHelpers;

import java.util.List;

import com.google.gson.JsonArray;

public class StringHelper {

	public static JsonArray stringListToArray(List<String> li) {
		JsonArray ja = new JsonArray();

		for (String s : li) {
			if (hasValue(ja, s)) continue;
			ja.add(s);
		}

		return ja;
	}

	public static boolean hasValue(JsonArray json, String value) {
	    for(int i = 0; i < json.size(); i++) {
	        if(json.get(i).getAsString().equals(value)) return true;
	    }
	    return false;
	}
	
}