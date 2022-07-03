package com.terturl.MMO.Util.JSONHelpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

	@SuppressWarnings("unchecked")
	public static JSONObject cubeToJSON(Cube c) {
		JSONObject jo = new JSONObject();
		jo.put("name", c.getName());
		
		JSONArray from = new JSONArray();
		for(Double d : c.getFrom()) {
			from.add(d);
		}
		jo.put("from", from);
		
		JSONArray to = new JSONArray();
		for(Double d : c.getTo()) {
			to.add(d);
		}
		jo.put("to", to);
		
		jo.put("rotation", rotationToJSON(c.getRotation()));
		
		JSONObject faces = new JSONObject();
		c.getFaces().forEach((k, v)-> {
			faces.put(k, faceToJSON(v));
		});
		jo.put("faces", faces);
		
		return jo;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject faceToJSON(Face face) {
		JSONObject jo = new JSONObject();
		
		JSONArray uv = new JSONArray();
		for(Double d : face.getUv()) {
			uv.add(d);
		}
		jo.put("uv", uv);
		jo.put("rotation", face.getRotation());
		jo.put("texture", face.getTexture());
		jo.put("tintindex", face.getTintindex());
		
		return jo;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject displayToJSON(Display dis) {
		JSONObject jo = new JSONObject();
		
		JSONArray translation = new JSONArray();
		for(double d : dis.getTranslation()) {
			translation.add(d);
		}
		jo.put("translation", translation);
		
		JSONArray rotation = new JSONArray();
		for(double d : dis.getRotation()) {
			rotation.add(d);
		}
		jo.put("rotation", rotation);
		
		JSONArray scale = new JSONArray();
		for(double d : dis.getScale()) {
			scale.add(d);
		}
		jo.put("scale", scale);
		
		return jo;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject rotationToJSON(Rotation rot) {
		JSONObject jo = new JSONObject();
		
		jo.put("angle", rot.getAngle());
		jo.put("axis", rot.getAxis());
		JSONArray origin = new JSONArray();
		for(double d : rot.getOrigin()) {
			origin.add(d);
		}
		jo.put("origin", origin);
		
		return jo;
	}
	
}