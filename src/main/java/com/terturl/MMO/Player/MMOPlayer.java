package com.terturl.MMO.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.datafixers.util.Pair;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.Items.SkullCreator;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;

/**
 * Handles the interaction and integration of all MMOClasses into a single
 * object as well as all quest viewing and completed quest viewing
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class MMOPlayer {

	@Getter
	private List<MMOClass> mmoClasses = new ArrayList<>();
	@Getter
	private List<NPC> playerSpecificNPCs = new ArrayList<>();
	@Getter
	@Setter
	private int currentCharacter = -1;

	@Getter
	@Setter
	private List<Location> blocksNotViewing = new ArrayList<>();

	@Getter
	private Map<UUID, Double> projectileMapping = new HashMap<>();

	@Getter
	@Setter
	private boolean hasUpdated = false;

	@Getter
	@Setter
	private boolean isInCombat = false;

	private List<EntityArmorStand> newQuestNotifiers = new ArrayList<>();
	private List<EntityArmorStand> completableQuestNofifiers = new ArrayList<>();

	@Getter
	@Setter
	private boolean showDamageTimer = true;
	@Getter
	@Setter
	private BossBar damageTimer;

	@Getter
	private BukkitRunnable combatTimer;

	@Getter
	private UUID playerUUID;
	@Getter
	private Player player;
	
	@Getter
	private List<Entity> damaged = new ArrayList<>();

	/**
	 * Creates a new MMOPlayer from the Player provided
	 * 
	 * @param p Player to hold information
	 */
	public MMOPlayer(Player p) {
		playerUUID = p.getUniqueId();
		player = p;
	}

	/**
	 * Used to check if the player is in combat or not, will create a bossbar above
	 * the players UI so they know when they are no longer in combat
	 */
	public void updateAndTakeDamage() {
		if (damageTimer != null) {
			damageTimer.removePlayer(player);
			damageTimer.removeAll();
		}

		if (showDamageTimer) {
			damageTimer = Bukkit.createBossBar("Combat Damage Timer", BarColor.RED, BarStyle.SEGMENTED_6);
			damageTimer.addFlag(BarFlag.DARKEN_SKY);
			damageTimer.addPlayer(player);
		}

		isInCombat = true;
		if (combatTimer != null) {
			combatTimer.cancel();
		}
		combatTimer = new BukkitRunnable() {
			double i = 60.0;

			@Override
			public void run() {
				if (i == 0.0) {
					isInCombat = false;
					if (damageTimer != null) {
						damageTimer.removePlayer(player);
						damageTimer.removeAll();
					}
					cancel();
				}
				if (damageTimer != null)
					damageTimer.setProgress(i / 60.0);
				i--;
			}
		};
		combatTimer.runTaskTimer(MinecraftMMO.getInstance(), 0, 20L);
	}

	/**
	 * Add a quest to the players current MMOClass
	 * 
	 * @param q
	 */
	public Quest addQuest(String q) {
		Quest quest = (Quest) MinecraftMMO.getInstance().getQuestManager().getQuest(q).clone();
		mmoClasses.get(currentCharacter).addQuest(quest);
		updateNPCQuests();
		return quest;
	}

	/**
	 * Update every NPC to check if they have a givable quest or if the player can
	 * complete a quest from that NPC
	 */
	public void updateNPCQuests() {
		MMOClass mc = mmoClasses.get(currentCharacter);

		// Destroy each EntityArmorStand in newQuestNotifiers
		newQuestNotifiers.stream().forEach(e -> {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(e.getId());
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
		});
		newQuestNotifiers.clear();

		// Destroy each EntityArmorStand in completableQuestNofifiers
		completableQuestNofifiers.stream().forEach(e -> {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(e.getId());
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
		});
		completableQuestNofifiers.clear();

		// Get all NPC that have more than 0 givable quests
		for (NPC npc : MinecraftMMO.getInstance().getNpcHandler().getNpcs()) {
			if (npc.getGivableQuest().size() > 0) {

				// Check all quests the NPC has and see the where the player is on that Quest
				for (String q : npc.getGivableQuest()) {
					// The player has completed the quest
					if (mc.hasCompletedQuest(q))
						continue;

					// The player has yet to turn the quest in
					if (mc.hasCompletableQuest(q) && mc.hasActiveQuest(q)) {
						WorldServer s = ((CraftWorld) player.getWorld()).getHandle();
						EntityArmorStand stand = new EntityArmorStand(EntityTypes.c, s);
						stand.setLocation(npc.getLocation().getX(), npc.getLocation().getY() + 1.0,
								npc.getLocation().getZ(), 0, 0);
						stand.setNoGravity(true);
						stand.setInvisible(true);

						completableQuestNofifiers.add(stand);
						continue;
					}

					// The player is currently working on the quest
					if (mc.hasActiveQuest(q))
						continue;

					// The player has completed all of the parent quests and is ready to pick up a
					// new quest
					if (MinecraftMMO.getInstance().getQuestManager().getParentQuestsForQuest(q).size() == 0
							|| mc.hasParentQuestsCompleted(q)) {
						WorldServer s = ((CraftWorld) player.getWorld()).getHandle();
						EntityArmorStand stand = new EntityArmorStand(EntityTypes.c, s);
						stand.setLocation(npc.getLocation().getX(), npc.getLocation().getY() + 1.0,
								npc.getLocation().getZ(), 0, 0);
						stand.setNoGravity(true);
						stand.setInvisible(true);

						newQuestNotifiers.add(stand);
					}
				}
			}
		}

		// Set all the ArmorStands head with a golden "?" skull and make it viewable to
		// the player
		for (EntityArmorStand eas : completableQuestNofifiers) {
			PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(eas.getId(), Arrays.asList(
					new Pair<EnumItemSlot, ItemStack>(EnumItemSlot.f, CraftItemStack.asNMSCopy(SkullCreator.getSkull(
							"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGEyZmRlMzRkMzRjODU4OGU1OGJmZDc5MGNlMTgwMjVmNzg0MzM5OWRlZTJhYjRjZWRjMmMwYjQ2M2ZkMWUifX19")))));
			PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(eas);
			PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(eas.getId(), eas.getDataWatcher(), true);
			eas.setInvisible(true);
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
			((CraftPlayer) player).getHandle().b.sendPacket(meta);
			((CraftPlayer) player).getHandle().b.sendPacket(equip);
		}

		// Set all the ArmorStands head with a golden "!" skull and make it viewable to
		// the player
		for (EntityArmorStand eas : newQuestNotifiers) {
			PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(eas.getId(), Arrays.asList(
					new Pair<EnumItemSlot, ItemStack>(EnumItemSlot.f, CraftItemStack.asNMSCopy(SkullCreator.getSkull(
							"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRlMWRhODgyZTQzNDgyOWI5NmVjOGVmMjQyYTM4NGE1M2Q4OTAxOGZhNjVmZWU1YjM3ZGViMDRlY2NiZjEwZSJ9fX0=")))));
			PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(eas);
			PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(eas.getId(), eas.getDataWatcher(), true);
			eas.setInvisible(true);
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
			((CraftPlayer) player).getHandle().b.sendPacket(meta);
			((CraftPlayer) player).getHandle().b.sendPacket(equip);
		}
	}

}