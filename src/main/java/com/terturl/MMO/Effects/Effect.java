package com.terturl.MMO.Effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Util.SoundInformation;

import lombok.Getter;

public abstract class Effect implements Cloneable {

	@Getter
	private List<SoundInformation> sounds = new ArrayList<>();
	
	public abstract void run(Player p);
	public abstract void load(JSONObject jo);
	
	public void addSound(SoundInformation si) {
		sounds.add(si);
	}
	
	public boolean hasDamaged(Player p, Entity e) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		if(mp.getDamaged().contains(e)) return true;
		return false;
	}
	
	public void addToDamaged(Player p, Entity e) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		mp.getDamaged().add(e);
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public enum LocationType {
		POINT, TARGETPLAYER;
	}
	
}