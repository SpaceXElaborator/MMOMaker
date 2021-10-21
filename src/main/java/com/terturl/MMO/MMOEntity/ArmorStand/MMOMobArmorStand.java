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

	public MMOMobArmorStand(Location loc, ArmorStandPart asp) {
		part = asp;
		small = asp.isSmall();
		offset = small ? -0.726D : -1.4375D;

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

	public void setPosition(Location loc) {
		ent.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
		PacketPlayOutEntityTeleport p1 = new PacketPlayOutEntityTeleport(ent);
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p1);
		}
	}

	public void setRotation(EulerAngle rot) {
		Vector3f headPose = toNMS(rot);
		if (ent.cg.equals(headPose))
			return;
		ent.setHeadPose(headPose);
		PacketPlayOutEntityMetadata p1 = new PacketPlayOutEntityMetadata(ent.getId(), ent.getDataWatcher(), true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p1);
		}
	}

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

	public void rotateHead(Float yaw, Float pitch) {
		PacketPlayOutEntityHeadRotation p1 = createDataSerializer(data->{
            data.d(ent.getId());
            data.writeByte((byte)((int)(yaw * 256.0F / 360.0F)));
            return new PacketPlayOutEntityHeadRotation(data);
        });
		PacketPlayOutEntityMetadata p2 = new PacketPlayOutEntityMetadata(ent.getId(), ent.getDataWatcher(), true);
		PacketPlayOutEntityLook p3 = new PacketPlayOutEntity.PacketPlayOutEntityLook(ent.getId(), (byte)((int)(yaw * 256.0F / 360.0F)), (byte)((int)(pitch * 256.0F / 360.0F)), true);
		for(Player p : Bukkit.getOnlinePlayers()) {
			sendPackets(p, p2, p3, p1);
		}
	}
	
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
		NetworkManager nm = (((CraftPlayer) p).getHandle()).b.a();
		for (Packet<?> packet : packets) {
			nm.sendPacket(packet);
		}
	}

	public void spawnToWorld(org.bukkit.World w) {
		WorldServer worldServer = ((CraftWorld) w).getHandle();
		worldServer.addEntity(ent);
	}

	private Vector3f toNMS(EulerAngle ea) {
		return new Vector3f((float) Math.toDegrees(ea.getX()), (float) Math.toDegrees(ea.getY()),
				(float) Math.toDegrees(ea.getZ()));
	}
	
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