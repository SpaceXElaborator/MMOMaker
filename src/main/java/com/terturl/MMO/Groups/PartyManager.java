package com.terturl.MMO.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manager of all MMOParties for creation, deletion, adding, and removing
 * players
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class PartyManager {

	private List<MMOParty> parties = new ArrayList<>();

	public PartyManager() {
	}

	/**
	 * Checks if the current player is in any MMOParty
	 * 
	 * @param uuid UUID of player
	 * @return If they are in a MMOParty or not
	 */
	public boolean isInParty(UUID uuid) {
		for (MMOParty p : parties) {
			if (p.containsPlayer(uuid))
				return true;
		}
		return false;
	}

	/**
	 * Get the MMOParty for any players UUID that is within that MMOParty
	 * 
	 * @param uuid UUID of player
	 * @return MMOParty or Null
	 */
	public MMOParty getPartyFromUUID(UUID uuid) {
		if (!isInParty(uuid))
			return null;
		for (MMOParty p : parties) {
			if (p.containsPlayer(uuid))
				return p;
		}
		return null;
	}

}