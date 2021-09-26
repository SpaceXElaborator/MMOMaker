package com.terturl.MMO.Abilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Abilities.Ability.AbilityCosts;
import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Effects.Effect.EffectType;
import com.terturl.MMO.Effects.Effect.LocationType;
import com.terturl.MMO.Effects.FireProjectile;
import com.terturl.MMO.Effects.LineEffect;
import com.terturl.MMO.Effects.Sphere;
import com.terturl.MMO.Effects.EffectTypes.LimitEffect;
import com.terturl.MMO.Effects.EffectTypes.RepeatingEffect;
import com.terturl.MMO.Effects.Util.ConeEffect;
import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Effects.Util.SoundInformation;
import com.terturl.MMO.Player.MMOClass;
import com.terturl.MMO.Util.JsonFileInterpretter;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class AbilityManager {
	
	@Getter
	private Map<String, Ability> abilities = new HashMap<String, Ability>();
	
	public AbilityManager() {
		File abDir = new File(MinecraftMMO.getInstance().getDataFolder(), "skills");
		if(!abDir.exists()) abDir.mkdir();
		for(File f : abDir.listFiles()) {
			if(f.getName().endsWith(".json")) {
				
				JsonFileInterpretter config = new JsonFileInterpretter(f);
				String name = config.getString("Name");
				String requiredClass = config.getString("Class");
				Integer levelRequied = config.getInt("Level");
				Map<AbilityCosts, Double> costs = new HashMap<>();
				JSONObject jo = config.getObject("Cost");
				if(jo.containsKey("Mana")) {
					costs.put(AbilityCosts.MANA, (Double)jo.get("Mana"));
				}
				if(jo.containsKey("Health")) {
					costs.put(AbilityCosts.HEALTH, (Double)jo.get("Health"));
				}
				
				Ability a = new Ability(name);
				a.setRequiredLevel(levelRequied);
				MMOClass mmoClass = MinecraftMMO.getInstance().getClassHandler().getClass(requiredClass);
				a.setMmoClass(mmoClass);
				a.setCosts(costs);
				
				JSONArray ja = config.getArray("Activate");
				a.getEffects().addAll(getEffects(ja, name));
				abilities.put(name, a);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Effect> getEffects(JSONArray ja, String name) {
		List<Effect> effectsList = new ArrayList<>();
		
		for(int i = 0; i < ja.size(); i++) {
			JSONObject temp = (JSONObject)ja.get(i);
			temp.forEach((k, v) -> {
				Object ef = null;
				JSONObject effect = (JSONObject) temp.get(k);
				String effectName = k.toString();
				EffectInformation ei = new EffectInformation();
				
				// Set basic information for the effect
				ei.setType((effect.containsKey("EffectType") ? EffectType.valueOf((String) effect.get("EffectType")) : EffectType.SINGLE));
				ei.setDelay((effect.containsKey("Delay") ? (long) effect.get("Delay") : 0));
				ei.setParticle((effect.containsKey("Particle") ? Particle.valueOf((String)effect.get("Particle")) : Particle.FLAME));
				ei.setParticleAmount((effect.containsKey("ParticleAmount") ? Integer.parseInt(effect.get("ParticleAmount").toString()) : 200));
				ei.setLocType((effect.containsKey("LocationType") ? LocationType.valueOf((String)effect.get("LocationType")) : LocationType.PLAYER));
				ei.setDamage((effect.containsKey("Damage") ? Double.parseDouble(effect.get("Damage").toString()) : 0.0));
				ei.setRange((effect.containsKey("Range") ? Double.parseDouble(effect.get("Range").toString()) : 10.0));
				
				if(effect.containsKey("Sounds")) {
					JSONArray sounds = (JSONArray) effect.get("Sounds");
					
					for(int x = 0; x < sounds.size(); x++) {
						JSONObject sound = (JSONObject) sounds.get(x);
						SoundInformation si = new SoundInformation();
						si.setSound((sound.containsKey("Sound") ? Sound.valueOf(sound.get("Sound").toString()) : Sound.BLOCK_GLASS_HIT));
						si.setVolume((sound.containsKey("Volume") ? Float.valueOf(sound.get("Volume").toString()) : 1.0f));
						si.setPitch((sound.containsKey("Pitch") ? Float.valueOf(sound.get("Pitch").toString()) : 1.0f));
						ei.getSounds().add(si);
					}
				}
				
				// Check for location offset
				if(effect.containsKey("Location")) {
					JSONObject loc = (JSONObject) effect.get("Location");
					ei.setXOff((loc.containsKey("X") ? (Double)loc.get("X") : 0.0));
					ei.setYOff((loc.containsKey("Y") ? (Double)loc.get("Y") : 0.0));
					ei.setZOff((loc.containsKey("Z") ? (Double)loc.get("Z") : 0.0));
				}
				
				if(performEffectTypeCheck(ei, effect, name, effectName)) {
					if(effectName.equals("Sphere")) {
						ef = new Sphere(ei);
						((Sphere) ef).setRadius((effect.containsKey("Size") ? (double) effect.get("Size") : 1.0));
					} else if(effectName.equals("Line")) {
						ef = new LineEffect(ei);
						((LineEffect) ef).setPenetrate((effect.containsKey("Penetrate") ? Boolean.parseBoolean(effect.get("Penetrate").toString()) : false));
					} else if(effectName.equals("Projectile")) {
						ef = new FireProjectile(ei);
						((FireProjectile) ef).setProjectile((effect.containsKey("Projectile") ? effect.get("Projectile").toString() : "Egg"));
					} else if(effectName.equals("Cone")) {
						ef = new ConeEffect(ei);
						((ConeEffect)ef).setDegree((effect.containsKey("Degree") ? Double.parseDouble(effect.get("Degree").toString()) : 90.0));
						JSONArray jo = (JSONArray) effect.get("Effects");
						List<Effect> coneEffects = getEffects(jo, name);
						for(Effect e : coneEffects) {
							((ConeEffect)ef).getEffects().add(e);
						}
					}
					if(ei.getType().equals(EffectType.REPEATING)) {
						RepeatingEffect re = new RepeatingEffect(ei, (Effect) ef);
						effectsList.add(re);
					} else if(ei.getType().equals(EffectType.LIMIT)) {
						LimitEffect le = new LimitEffect(ei, (Effect)ef);
						effectsList.add(le);
					} else {
						effectsList.add((Effect) ef);
					}
				}
			});
			
		}
		
		return effectsList;
	}
	
	private boolean performEffectTypeCheck(EffectInformation ei, JSONObject effect, String name, String k) {
		// Checks for missing values in repeating
		if(ei.getType().equals(EffectType.REPEATING)) {
			if(!effect.containsKey("Duration")) {	
				MinecraftMMO.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + name + "] - Missing value `Duration` for repeating effect " + k.toString());
				return false;
			}
			if(!effect.containsKey("Every")) {
				MinecraftMMO.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + name + "] - Missing value `Every` for repeating effect " + k.toString());
				return false;
			}
			ei.setDuration((long) effect.get("Duration"));
			ei.setEvery((long) effect.get("Every"));
		}
		
		if(k.equalsIgnoreCase("Cone")) {
			if(!effect.containsKey("Effects")) {
				MinecraftMMO.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + name + "] - Missing value `Effects` for Cone effect " + k.toString());
				return false;
			}
		}
		
		if(ei.getType().equals(EffectType.LIMIT)) {
			if(!effect.containsKey("Limit")) {	
				MinecraftMMO.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + name + "] - Missing value `Limit` for Limited effect " + k.toString());
				return false;
			}
			if(!effect.containsKey("Every")) {
				MinecraftMMO.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + name + "] - Missing value `Every` for repeating effect " + k.toString());
				return false;
			}
			ei.setLimitTimes(Integer.valueOf(effect.get("Limit").toString()));
			ei.setEvery((long) effect.get("Every"));
		}
		
		return true;
	}
	
}