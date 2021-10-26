package com.terturl.MMO.Util.JSONHelpers;

import java.util.List;

import org.json.simple.JSONArray;

public class StringHelper {

	@SuppressWarnings("unchecked")
	public static JSONArray stringListToArray(List<String> li) {
		JSONArray ja = new JSONArray();

		for (String s : li) {
			if (ja.contains(s))
				continue;
			ja.add(s);
		}

		return ja;
	}

}