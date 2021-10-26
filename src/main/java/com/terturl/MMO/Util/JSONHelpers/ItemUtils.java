package com.terturl.MMO.Util.JSONHelpers;

import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.Items.CustomItem;

/**
 * Saves and loads CustomItems to/from Json strings
 * @author Sean Rahman
 * @since 0.27.0
 */
public class ItemUtils {
	
	@SuppressWarnings("unchecked")
	public static JSONObject itemToJSON(CustomItem ci) {
		JSONObject jo = new JSONObject();
		jo.put("Name", ci.getName());
		return jo;
	}
	
	public static CustomItem JSONToItem(JSONObject jo) {
		CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(jo.get("Name").toString());
		return ci;
	}
	
}