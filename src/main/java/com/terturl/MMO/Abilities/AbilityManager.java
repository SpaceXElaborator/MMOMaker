package com.terturl.MMO.Abilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

				JsonObject config = new JsonFileInterpretter(f).getJson();
				String name = config.get("Name").getAsString();
				Integer levelRequied = config.get("Level").getAsInt();
				Map<AbilityCosts, Double> costs = new HashMap<>();
				JsonObject joCost = config.get("Cost").getAsJsonObject();
				if (joCost.has("Mana")) {
					costs.put(AbilityCosts.MANA, joCost.get("Mana").getAsDouble());
				}
				if (joCost.has("Health")) {
					costs.put(AbilityCosts.HEALTH, joCost.get("Health").getAsDouble());
				}

				Ability a = new Ability(name);
				a.setRequiredLevel(levelRequied);
				a.setCosts(costs);

				JsonArray ja = config.get("Activate").getAsJsonArray();
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

	private List<Effect> getEffects(JsonArray ja, String name) {
		List<Effect> effectsList = new ArrayList<>();
		
		for(int i = 0; i < ja.size(); i++) {
			JsonObject effectListing = ja.get(i).getAsJsonObject();
			for(Entry<String, JsonElement> je : effectListing.entrySet()) {
				if(!effects.containsKey(je.getKey())) continue;
				if(!je.getValue().isJsonObject()) continue;
				Effect e = effects.get(je.getKey());
				e.load(je.getValue().getAsJsonObject());
			}
		}
		
		return effectsList;
	}

}