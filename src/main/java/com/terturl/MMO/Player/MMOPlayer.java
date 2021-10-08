package com.terturl.MMO.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.datafixers.util.Pair;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
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

public class MMOPlayer {

	@Getter
	private List<MMOClass> mmoClasses = new ArrayList<>();
	@Getter
	private List<NPC> playerSpecificNPCs = new ArrayList<>();
	@Getter @Setter
	private int currentCharacter;
	
	@Getter
	private Map<UUID, Double> projectileMapping = new HashMap<>();
	
	@Getter @Setter
	private boolean hasUpdated = false;
	
	@Getter @Setter
	private boolean isInCombat = false;
	
	private List<EntityArmorStand> newQuestNotifiers = new ArrayList<>();
	private List<EntityArmorStand> completableQuestNofifiers = new ArrayList<>();
	
	@Getter @Setter
	private boolean showDamageTimer = true;
	@Getter @Setter
	private BossBar damageTimer;
	
	@Getter
	private BukkitRunnable combatTimer;
	
	@Getter
	private UUID playerUUID;
	@Getter
	private Player player;
	
	public MMOPlayer(Player p) {
		playerUUID = p.getUniqueId();
		player = p;
	}
	
	public void updateAndTakeDamage() {
		if(damageTimer != null) {
			damageTimer.removePlayer(player);
			damageTimer.removeAll();
		}
		
		if(showDamageTimer) {
			damageTimer = Bukkit.createBossBar("Combat Damage Timer", BarColor.RED, BarStyle.SEGMENTED_6);
			damageTimer.addFlag(BarFlag.DARKEN_SKY);
			damageTimer.addPlayer(player);
		}
		
		isInCombat = true;
		if(combatTimer != null) {
			combatTimer.cancel();
		}
		combatTimer = new BukkitRunnable() {
			double i = 60.0;
			@Override
			public void run() {
				if(i == 0.0) {
					isInCombat = false;
					if(damageTimer != null) {
						damageTimer.removePlayer(player);
						damageTimer.removeAll();
					}
					cancel();
				}
				if(damageTimer != null) damageTimer.setProgress(i/60.0);
				i--;
			}
		};
		combatTimer.runTaskTimer(MinecraftMMO.getInstance(), 0, 20L);
	}
	
	public void updateNPCQuests() {
		newQuestNotifiers.stream().forEach(e -> {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(e.getId());
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
		});
		completableQuestNofifiers.stream().forEach(e -> {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(e.getId());
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
		});
		newQuestNotifiers.clear();
		completableQuestNofifiers.clear();
		for (NPC npc : MinecraftMMO.getInstance().getNpcHandler().getNpcs()) {
			if(npc.getGivableQuest().size() > 0) {
				for(Quest q : npc.getGivableQuest()) {
					if(mmoClasses.get(currentCharacter).hasActiveQuest(q)) continue;
					if(mmoClasses.get(currentCharacter).hasCompletedQuest(q)) continue;
					if(q.getParentQuests().size() == 0 || mmoClasses.get(currentCharacter).hasParentQuestsCompleted(q)) {
						WorldServer s = ((CraftWorld) player.getWorld()).getHandle();
						EntityArmorStand stand = new EntityArmorStand(EntityTypes.c, s);
						stand.setLocation(npc.getLocation().getX(), npc.getLocation().getY() + 1.0, npc.getLocation().getZ(), 0,
								0);
						stand.setNoGravity(true);
						stand.setInvisible(true);
						
						newQuestNotifiers.add(stand);
					}
				}
			}
		}
		
		for(EntityArmorStand eas : newQuestNotifiers) {
			PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(eas.getId(),
					Arrays.asList(new Pair<EnumItemSlot, ItemStack>(EnumItemSlot.f,
							CraftItemStack.asNMSCopy(SkullCreator.getSkull(
									"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlNTIyZDkxODI1MjE0OWU2ZWRlMmVkZjNmZTBmMmMyYzU4ZmVlNmFjMTFjYjg4YzYxNzIwNzIxOGFlNDU5NSJ9fX0=")))));
			PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(eas);
			PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(eas.getId(), eas.getDataWatcher(), true);
			eas.setInvisible(true);
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
			((CraftPlayer) player).getHandle().b.sendPacket(meta);
			((CraftPlayer) player).getHandle().b.sendPacket(equip);
		}
	}
	
}