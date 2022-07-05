package com.terturl.MMO.Util.JSONHelpers;

import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.Items.CustomItem;

/**
 * Saves and loads CustomItems to/from Json strings
 * @author Sean Rahman
 * @since 0.27.0
 */
public class ItemUtils {
	
	public static JsonObject itemToJSON(CustomItem ci) {
		JsonObject jo = new JsonObject();
		jo.addProperty("Name", ci.getName());
		return jo;
	}
	
	public static CustomItem JSONToItem(JsonObject jo) {
		CustomItem ci = MinecraftMMO.getInstance().getItemManager().getItem(jo.get("Name").getAsString());
		return ci;
	}
	
}