package com.terturl.MMO.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.terturl.MMO.Player.MMOPlayer;

import lombok.Getter;
import lombok.Setter;

/**
 * Party group to handle a non-permanent group of players for the sharing of XP
 * and quests
 * 
 * @author Sean Rahman
 * @since 0.64.0
 *
 */
public class MMOParty {

	@Getter
	private List<UUID> players = new ArrayList<>();

	@Getter
	@Setter
	private MMOPlayer owner;

	/**
	 * Create a new MMOParty with MMOPlayer as the leader of the party
	 * 
	 * @param mp MMOPlayer owner
	 */
	public MMOParty(MMOPlayer mp) {
		owner = mp;
		players.add(mp.getPlayerUUID());
	}

	/**
	 * Add a player to the Party by UUID
	 * 
	 * @param uuid UUID of player
	 * @return If they were added or not
	 */
	public boolean addPlayer(UUID uuid) {
		if (players.contains(uuid))
			return false;
		players.add(uuid);
		return true;
	}

	/**
	 * Remove a player from the party by UUID
	 * 
	 * @param uuid UUID of player
	 * @return If they were removed or not
	 */
	public boolean removePlayer(UUID uuid) {
		if (!players.contains(uuid))
			return false;
		players.remove(uuid);
		return true;
	}

	/**
	 * Checks if the player is currently in the MMOParty
	 * 
	 * @param uuid UUID of player
	 * @return If they are in the MMOParty or not
	 */
	public boolean containsPlayer(UUID uuid) {
		return players.contains(uuid);
	}

}