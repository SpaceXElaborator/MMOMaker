package com.terturl.MMO.Util.Listeners;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.Events.ClickClassNPCEvent;
import com.terturl.MMO.Util.Events.ClickNPCEvent;
import com.terturl.MMO.Util.Events.ClickPlayerClassNPCEvent;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.world.EnumHand;

public class PacketListener {

	public static void injectPlayer(Player p) {
		ChannelDuplexHandler handler = new ChannelDuplexHandler() {

			@Override
			public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
				// MinecraftMMO.getInstance().getLogger().log(Level.INFO, "PACKET READ: " + packet.toString());
				if (packet instanceof PacketPlayInUseEntity) {
					try {
						PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet;
						// Obtain the EntityID from the private field a
						Field entityId = useEntity.getClass().getDeclaredField("a");
						entityId.setAccessible(true);
						int id = entityId.getInt(useEntity);
						
						// Obtain the class that extends EnumEntityUseAction
						Field someValue = useEntity.getClass().getDeclaredField("b");
						someValue.setAccessible(true);
						Object test = someValue.get(useEntity);
						
						// Obtain if the player is crouching
						Field isCrouching = useEntity.getClass().getDeclaredField("c");
						isCrouching.setAccessible(true);
						Boolean bool = isCrouching.getBoolean(useEntity);
						
						// Get all classes and only obtain the class we need to check for interacting
						Class<?>[] testClasses = PacketPlayInUseEntity.class.getDeclaredClasses();
						Class<?> testClass = null;
						for(Class<?> c : testClasses) {
							if(c.getName().endsWith("d")) {
								testClass = c;
							}
						}
						
						if(test.getClass() == testClass) {
							Field enumHand = testClass.getDeclaredField("a");
							enumHand.setAccessible(true);
							Object hand = enumHand.get(test);
							// This will only send 1 for either a or b, but what is both? They seem to both work when right clicking
							if(hand == EnumHand.b) {
								if(MinecraftMMO.getInstance().getNpcHandler().isNpc(id)) {
									Future<ClickNPCEvent> temp = Bukkit.getScheduler().callSyncMethod(MinecraftMMO.getInstance(), new Callable<ClickNPCEvent>() {
										@Override
										public ClickNPCEvent call() throws Exception {
											ClickNPCEvent e = new ClickNPCEvent(MinecraftMMO.getInstance().getNpcHandler().getNPC(id), p, bool);
											return e;
										}
									});
									
									Bukkit.getScheduler().runTaskLater(MinecraftMMO.getInstance(), new Runnable() {
										@Override
										public void run() {
											if(temp.isDone()) {
												try {
													if(temp.get().isCancelled()) return;
													Bukkit.getPluginManager().callEvent(temp.get());
												} catch (InterruptedException | ExecutionException e) {
													e.printStackTrace();
												}
											}
										}
									}, 1l);
								} else if(MinecraftMMO.getInstance().getNpcHandler().isClassNpc(id)) {
									Future<ClickClassNPCEvent> temp = Bukkit.getScheduler().callSyncMethod(MinecraftMMO.getInstance(), new Callable<ClickClassNPCEvent>() {
										@Override
										public ClickClassNPCEvent call() throws Exception {
											ClickClassNPCEvent e = new ClickClassNPCEvent(MinecraftMMO.getInstance().getNpcHandler().getClassNPC(id), p, bool);
											return e;
										}
									});
									
									Bukkit.getScheduler().runTaskLater(MinecraftMMO.getInstance(), new Runnable() {
										@Override
										public void run() {
											if(temp.isDone()) {
												try {
													if(temp.get().isCancelled()) return;
													Bukkit.getPluginManager().callEvent(temp.get());
												} catch (InterruptedException | ExecutionException e) {
													e.printStackTrace();
												}
											}
										}
									}, 1l);
								} else if(MinecraftMMO.getInstance().getPlayerHandler().isPlayerClassNPC(p, id)) {
									Future<ClickPlayerClassNPCEvent> temp = Bukkit.getScheduler().callSyncMethod(MinecraftMMO.getInstance(), new Callable<ClickPlayerClassNPCEvent>() {
										@Override
										public ClickPlayerClassNPCEvent call() throws Exception {
											ClickPlayerClassNPCEvent e = new ClickPlayerClassNPCEvent(MinecraftMMO.getInstance().getPlayerHandler().getPlayerClassNPC(p, id), p, bool);
											return e;
										}
									});
									
									Bukkit.getScheduler().runTaskLater(MinecraftMMO.getInstance(), new Runnable() {
										@Override
										public void run() {
											if(temp.isDone()) {
												try {
													if(temp.get().isCancelled()) return;
													Bukkit.getPluginManager().callEvent(temp.get());
												} catch (InterruptedException | ExecutionException e) {
													e.printStackTrace();
												}
											}
										}
									}, 1l);
								}else {
									return;
								}
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.channelRead(context, packet);
			}

			@Override
			public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
				// MinecraftMMO.getInstance().getLogger().log(Level.INFO, "PACKET WRITE: " + packet.toString());
				super.write(ctx, packet, promise);
			}

		};
		ChannelPipeline pipeline = ((CraftPlayer) p).getHandle().b.a.k.pipeline();
		pipeline.addBefore("packet_handler", p.getName(), handler);
	}
	
	public static void removePlayer(Player p) {
		Channel channel = ((CraftPlayer) p).getHandle().b.a.k;
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(p.getName());
			return null;
		});
	}
	
}