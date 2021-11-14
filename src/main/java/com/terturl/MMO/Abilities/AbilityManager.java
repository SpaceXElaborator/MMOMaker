package com.terturl.MMO.Abilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Abilities.Ability.AbilityCosts;
import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Util.JsonFileInterpretter;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * Handles the loading of effects and creation of abilities into the Java memory
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class AbilityManager {

	@Getter
	private Map<String, Ability> abilities = new HashMap<>();

	@Getter
	private Map<String, Effect> effects = new HashMap<>();
	
	private File abDir;
	
	public AbilityManager() {
		abDir = new File(MinecraftMMO.getInstance().getDataFolder(), "abilities");
		if (!abDir.exists())
			abDir.mkdir();
	}
	
	public void registerEffect(String name, Effect effectBase) {
		effects.put(name, effectBase);
	}
	
	public void addAbilities() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Abilities...");
		for (File f : abDir.listFiles()) {
			if (f.getName().endsWith(".json")) {

				JsonFileInterpretter config = new JsonFileInterpretter(f);
				String name = config.getString("Name");
				Integer levelRequied = config.getInt("Level");
				Map<AbilityCosts, Double> costs = new HashMap<>();
				JSONObject jo = config.getObject("Cost");
				if (jo.containsKey("Mana")) {
					costs.put(AbilityCosts.MANA, (Double) jo.get("Mana"));
				}
				if (jo.containsKey("Health")) {
					costs.put(AbilityCosts.HEALTH, (Double) jo.get("Health"));
				}

				Ability a = new Ability(name);
				a.setRequiredLevel(levelRequied);
				a.setCosts(costs);

				JSONArray ja = config.getArray("Activate");
				a.getEffects().addAll(getEffects(ja, name));
				abilities.put(name, a);
			}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}
	
	public Ability getAbility(String s) {
		if(abilities.containsKey(s)) {
			return abilities.get(s);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Effect> getEffects(JSONArray ja, String name) {
		List<Effect> effectsList = new ArrayList<>();
		
		for(int i = 0; i < ja.size(); i++) {
			JSONObject effectListing = (JSONObject)ja.get(i);
			effectListing.forEach((k, v) -> {
				if(!effects.containsKey(k.toString())) return;
				Effect e = effects.get(k);
				e.load((JSONObject)v);
			});
		}
		
		return effectsList;
	}

}