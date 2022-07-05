package com.terturl.MMO.Util.JSONHelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Cube;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Display;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Face;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Rotation;

/**
 * Class that will turn any ResourcePack object into a JSON string to be saved to a file for the Resource Pack generation process
 * @author Sean Rahman
 * @since 0.58.0
 * @see com.terturl.MMO.MMOEntity.ResourcePack.Elements.Cube
 * @see com.terturl.MMO.MMOEntity.ResourcePack.Elements.Display
 * @see com.terturl.MMO.MMOEntity.ResourcePack.Elements.Face
 * @see com.terturl.MMO.MMOEntity.ResourcePack.Elements.Rotation
 *
 */
public class ResourcePackJSON {

	public static JsonObject cubeToJSON(Cube c) {
		JsonObject jo = new JsonObject();
		jo.addProperty("name", c.getName());
		
		JsonArray from = new JsonArray();
		for(double d : c.getFrom()) {
			from.add(d);
		}
		jo.add("from", from);
		
		JsonArray to = new JsonArray();
		for(double d : c.getTo()) {
			to.add(d);
		}
		jo.add("to", to);
		
		jo.add("rotation", rotationToJSON(c.getRotation()));
		
		JsonObject faces = new JsonObject();
		
		c.getFaces().forEach((k, v)-> {
			faces.add(k, faceToJSON(v));
		});
		jo.add("faces", faces);
		
		return jo;
	}
	
	public static JsonObject faceToJSON(Face face) {
		JsonObject jo = new JsonObject();
		
		JsonArray uv = new JsonArray();
		for(double d : face.getUv()) {
			uv.add(d);
		}
		jo.add("uv", uv);
		jo.addProperty("rotation", face.getRotation());
		jo.addProperty("texture", face.getTexture());
		jo.addProperty("tintindex", face.getTintindex());
		
		return jo;
	}
	
	public static JsonObject displayToJSON(Display dis) {
		JsonObject jo = new JsonObject();
		
		JsonArray translation = new JsonArray();
		for(double d : dis.getTranslation()) {
			translation.add(d);
		}
		jo.add("translation", translation);
		
		JsonArray rotation = new JsonArray();
		for(double d : dis.getRotation()) {
			rotation.add(d);
		}
		jo.add("rotation", rotation);
		
		JsonArray scale = new JsonArray();
		for(double d : dis.getScale()) {
			scale.add(d);
		}
		jo.add("scale", scale);
		
		return jo;
	}
	
	public static JsonObject rotationToJSON(Rotation rot) {
		JsonObject jo = new JsonObject();
		
		jo.addProperty("angle", rot.getAngle());
		jo.addProperty("axis", rot.getAxis());
		JsonArray origin = new JsonArray();
		for(double d : rot.getOrigin()) {
			origin.add(d);
		}
		jo.add("origin", origin);
		
		return jo;
	}
	
}