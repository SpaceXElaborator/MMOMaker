package com.terturl.MMO.Commands.Debug;

import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.MMOEntity;
import com.terturl.MMO.Framework.CraftCommand;

import net.minecraft.server.level.WorldServer;

public class SpawnMMOEntity extends CraftCommand {

	public SpawnMMOEntity() {
		super("spawn-entity");
	}
	
	public void handleCommand(Player p, String[] args) {
		WorldServer world = ((CraftWorld)p.getLocation().getWorld()).getHandle();
		MMOEntity me = MinecraftMMO.getInstance().getEntityManager().getEntity(args[0]).clone();
		world.addEntity(me);
		me.safeTeleport(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), false, TeleportCause.COMMAND);
	}
	
}