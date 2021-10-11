package com.terturl.MMO.Util.JSONHelpers;

import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.Items.CustomItem;

public class ItemUtils {

	// TODO: Save durability
	
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