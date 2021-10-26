package com.terturl.MMO.MMOEntity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import com.mojang.datafixers.util.Pair;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.core.Vector3f;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributes;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;

/**
 * The base class of an ArmorStand that will be spawned into the world and the
 * newly created BBOCube/Cube will be placed as the helmet slot to be used for
 * representing a mob in game
 * 
 * @author Sean Rahman
 * @since 0.59.0
 *
 */
public class MMOMobArmorStand {

	private List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> items = new ArrayList<>();

	@Getter
	private EntityArmorStand ent;
	private ItemStack item = new ItemStack(Material.SADDLE);

	@Getter
	@Setter
	private boolean small;

	@Getter
	@Setter
	private Double offset;

	@Getter
	@Setter
	private ArmorStandPart part;

	@Getter
	@Setter
	private Boolean itemVisible = true;

	/**
	 * Creates a new ArmorStand at the location with the given ArmorStandPart
	 * information to be used for representing models
	 * 
	 * @param loc Location to spawn ArmorStand
	 * @param asp Part to acquire information about placement and model
	 */
	public MMOMobArmorStand(Location loc, ArmorStandPart asp) {
		part = asp;
		small = asp.isSmall();

		// Math to make sure all parts add up correctly, if just using one or the other,
		// if the model is to small, can make large gaps between armor stand parts
		offset = small ? -0.726D : -1.4375D;

		// Create a new Invisible EntityArmorStand with the given location and set its
		// head slot to Saddle
		WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
		ent = new EntityArmorStand((World) worldServer, loc.getX(), loc.getY() + offset, loc.getZ());
		ent.setNoGravity(true);
		NBTTagCompound tag = new NBTTagCompound();
		ent.e(tag);
		tag.setInt("DisabledSlots", 4144959);
		ent.load(tag);
		ent.setInvisible(true);
		ent.setMarker(true);
		ent.setSilent(true);
		ent.setSmall(small);
		setSlot(EnumItemSlot.f, CraftItemStack.asNMSCopy(item));
	}

	/**
	 * Sets the ArmorStand's location to the given location and send a packet that
	 * the ArmorStand teleported to all players on the server
	 * 
	 * @param loc Location to send ArmorStand
	 */
	public void setPosition(Location loc) {
		ent.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
		PacketPlayOutEntityTeleport p1 = new PacketPlayOutEntityTeleport(ent);
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p1);
		}
	}

	/**
	 * Sets the ArmorStands head rotation for movement of the MMOMob model. This
	 * will use the origin as the rotation point but it is not coded as the
	 * Minecrafts ResourcePack does this for us
	 * 
	 * @param rot Rotation to set the head
	 */
	public void setRotation(EulerAngle rot) {
		// setHeadPose needs a Vector3f from Minecrafts src code, so we need to
		// translate our EulerAngle into this
		Vector3f headPose = toNMS(rot);
		if (ent.cg.equals(headPose)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Equals");
			return;
		}
		ent.setHeadPose(headPose);
		PacketPlayOutEntityMetadata p1 = new PacketPlayOutEntityMetadata(ent.getId(), ent.getDataWatcher(), true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p1);
		}

	}

	/**
	 * Sets the Items CustomModelData to the provided integer to display the correct
	 * model from saddle.json created in the generate Resource Pack
	 * 
	 * @param i CustomItemModel integer
	 */
	public void setMeta(Integer i) {
		ItemMeta im = item.getItemMeta();
		im.setCustomModelData(i);
		item.setItemMeta(im);
		setSlot(EnumItemSlot.f, CraftItemStack.asNMSCopy(item));
		PacketPlayOutEntityEquipment p1 = new PacketPlayOutEntityEquipment(ent.getId(), items);
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p1);
		}
	}

	/**
	 * Spawn ArmorStand for the following player with all metadata and equipment
	 * needed to show the model
	 * 
	 * @param p Player to see ArmorStand
	 */
	public void spawn(Player p) {
		PacketPlayOutSpawnEntityLiving p1 = new PacketPlayOutSpawnEntityLiving(ent);
		PacketPlayOutEntityMetadata p2 = new PacketPlayOutEntityMetadata(ent.getId(), ent.getDataWatcher(), true);
		PacketPlayOutUpdateAttributes p3 = new PacketPlayOutUpdateAttributes(ent.getId(), ent.getAttributeMap().b());
		PacketPlayOutUpdateAttributes p4 = new PacketPlayOutUpdateAttributes(ent.getId(), ent.getAttributeMap().b());
		if (getItemVisible()) {
			PacketPlayOutEntityEquipment p5 = new PacketPlayOutEntityEquipment(ent.getId(), items);
			sendPackets(p, p1, p2, p3, p5, p4);
		} else {
			sendPackets(p, p1, p2, p3, p4);
		}
	}

	/**
	 * Sets where the MMOMob is looking at. This should only be used on the Head
	 * BBOBone, all else should use the above rotation method. Currently, pitch does
	 * not work
	 * 
	 * @param yaw   Yaw to set the head
	 * @param pitch Pitch to set the head
	 */
	public void rotateHead(Float yaw, Float pitch) {
		PacketPlayOutEntityHeadRotation p1 = createDataSerializer(data -> {
			data.d(ent.getId());
			data.writeByte((byte) ((int) (yaw * 256.0F / 360.0F)));
			return new PacketPlayOutEntityHeadRotation(data);
		});
		PacketPlayOutEntityMetadata p2 = new PacketPlayOutEntityMetadata(ent.getId(), ent.getDataWatcher(), true);
		PacketPlayOutEntityLook p3 = new PacketPlayOutEntity.PacketPlayOutEntityLook(ent.getId(),
				(byte) ((int) (yaw * 256.0F / 360.0F)), (byte) ((int) (pitch * 256.0F / 360.0F)), true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p2, p3, p1);
		}
	}

	/**
	 * Destroy the ArmorStand and send the packet to the player so they will stop
	 * seeing the ArmorStand
	 * 
	 * @param p Player to stop seeing ArmorStand
	 */
	public void despawn(Player p) {
		PacketPlayOutEntityDestroy des = new PacketPlayOutEntityDestroy(ent.getId());
		sendPackets(p, des);
	}

	private void setSlot(EnumItemSlot slot, net.minecraft.world.item.ItemStack item) {
		ent.setSlot(slot, item);
		items.clear();
		items.add(new Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>(slot, item));
	}

	private void sendPackets(Player p, Packet<?>... packets) {
		// Get the players NetworkManager and send all packets provided
		NetworkManager nm = (((CraftPlayer) p).getHandle()).b.a();
		for (Packet<?> packet : packets) {
			nm.sendPacket(packet);
		}
	}

	// Currently Not Used
	public void spawnToWorld(org.bukkit.World w) {
		WorldServer worldServer = ((CraftWorld) w).getHandle();
		worldServer.addEntity(ent);
	}

	private Vector3f toNMS(EulerAngle ea) {
		// EulerAngle to Vector3f is quite single, just turn all values into Degrees
		return new Vector3f((float) Math.toDegrees(ea.getX()), (float) Math.toDegrees(ea.getY()),
				(float) Math.toDegrees(ea.getZ()));
	}

	// Used to create a new Packet metadata to edit specific values from the Packet
	private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
		PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
		T result = null;
		try {
			result = callback.apply(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.release();
		}
		return result;
	}

	@FunctionalInterface
	private interface UnsafeFunction<K, T> {
		T apply(K k) throws Exception;
	}

}