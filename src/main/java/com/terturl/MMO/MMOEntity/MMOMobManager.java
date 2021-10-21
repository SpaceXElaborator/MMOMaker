package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class MMOMobManager {
	
	@Getter
	private List<MMOMob> mobs = new ArrayList<>();
	
	public void registerMob(MMOMobEntity me) {
		MMOMob m = new MMOMob(me);
		mobs.add(m);
	}
	
}