package com.terturl.MMO.CustomArmorStandMobs.Boss;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.CustomArmorStandMobs.ArmorStandMob;
import com.terturl.MMO.CustomArmorStandMobs.Utils.ArmorStandContainer;
import com.terturl.MMO.CustomArmorStandMobs.Utils.RelativeLocation;
import com.terturl.MMO.Util.Items.SkullCreator;

import lombok.Getter;

public class MagmaBoss extends ArmorStandMob {

	private List<ArmorStandContainer> rightArm = new ArrayList<>();
	private ArmorStandContainer rightArmP;
	private List<ArmorStandContainer> leftArm = new ArrayList<>();
	private ArmorStandContainer leftArmP;
	private List<ArmorStandContainer> rightLeg = new ArrayList<>();
	private ArmorStandContainer rightLegP;
	private List<ArmorStandContainer> leftLeg = new ArrayList<>();
	private ArmorStandContainer leftLegP;
	
	public MagmaBoss(Location loc) {
		super(loc);
		
		ArmorStandContainer head = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.75, 0, 0), false);
		head.setHelmet(MagmaBossParts.HEAD.getStack());
		
		// Torso
		ArmorStandContainer body1 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.22, -0.25, 0.3), false);
		body1.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body2 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.22, -0.25, -0.3), false);
		body2.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body3 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.22, 0.25, 0.3), false);
		body3.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body4 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.22, 0.25, -0.3), false);
		body4.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body5 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.19, 0.5, -0.1), false);
		body5.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body6 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.19, 0.5, 0.1), false);
		body6.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body7 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.19, -0.5, -0.1), false);
		body7.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body8 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.19, -0.5, 0.1), false);
		body8.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body9 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.75, -0.2, 0.25), false);
		body9.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body10 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.75, -0.2, -0.25), false);
		body10.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body11 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.75, 0.2, 0.25), false);
		body11.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body12 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.75, 0.2, -0.25), false);
		body12.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body13 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.19, -0.15, 0.2), false);
		body13.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body14 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.19, -0.15, -0.2), false);
		body14.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body15 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.19, 0.15, 0.2), false);
		body15.setHelmet(MagmaBossParts.BODY.getStack());
		ArmorStandContainer body16 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.19, 0.15, -0.2), false);
		body16.setHelmet(MagmaBossParts.BODY.getStack());
		
		// RightArm
		ArmorStandContainer rightShoulder = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.15, -1, 0), false);
		rightShoulder.setHelmet(MagmaBossParts.SHOULDER.getStack());
		ArmorStandContainer rightArm1 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.4, -1, 0), true);
		rightArm1.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer rightArm2 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.1, -1, 0), true);
		rightArm2.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer rightArm3 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.7, -1, 0), true);
		rightArm3.setHelmet(MagmaBossParts.ARM.getStack());
		
		// LeftArm
		ArmorStandContainer leftShoulder = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.15, 1, 0), false);
		leftShoulder.setHelmet(new ItemStack(MagmaBossParts.SHOULDER.getStack()));
		ArmorStandContainer leftArm1 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.5, 1, 0), true);
		leftArm1.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer leftArm2 = new ArmorStandContainer(loc.clone(), new RelativeLocation(1.1, 1, 0), true);
		leftArm2.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer leftArm3 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.7, 1, 0), true);
		leftArm3.setHelmet(MagmaBossParts.ARM.getStack());
		
		// Left Leg
		ArmorStandContainer leftHip = new ArmorStandContainer(loc.clone(), new RelativeLocation(-0.25, 0.4, 0), false);
		leftHip.setHelmet(new ItemStack(MagmaBossParts.SHOULDER.getStack()));
		ArmorStandContainer leftLeg1 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.1, 0.4, 0), true);
		leftLeg1.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer leftLeg2 = new ArmorStandContainer(loc.clone(), new RelativeLocation(-0.3, 0.4, 0), true);
		leftLeg2.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer leftLeg3 = new ArmorStandContainer(loc.clone(), new RelativeLocation(-0.7, 0.4, 0), true);
		leftLeg3.setHelmet(MagmaBossParts.ARM.getStack());
		
		// Left Leg
		ArmorStandContainer rightHip = new ArmorStandContainer(loc.clone(), new RelativeLocation(-0.25, -0.4, 0), false);
		rightHip.setHelmet(new ItemStack(MagmaBossParts.SHOULDER.getStack()));
		ArmorStandContainer rightLeg1 = new ArmorStandContainer(loc.clone(), new RelativeLocation(0.1, -0.4, 0), true);
		rightLeg1.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer rightLeg2 = new ArmorStandContainer(loc.clone(), new RelativeLocation(-0.3, -0.4, 0), true);
		rightLeg2.setHelmet(MagmaBossParts.ARM.getStack());
		ArmorStandContainer rightLeg3 = new ArmorStandContainer(loc.clone(), new RelativeLocation(-0.7, -0.4, 0), true);
		rightLeg3.setHelmet(MagmaBossParts.ARM.getStack());
		
		parts.put("head", head);
		parts.put("body1", body1);
		parts.put("body2", body2);
		parts.put("body3", body3);
		parts.put("body4", body4);
		parts.put("body5", body5);
		parts.put("body6", body6);
		parts.put("body7", body7);
		parts.put("body8", body8);
		parts.put("body9", body9);
		parts.put("body10", body10);
		parts.put("body11", body11);
		parts.put("body12", body12);
		parts.put("body13", body13);
		parts.put("body14", body14);
		parts.put("body15", body15);
		parts.put("body16", body16);
		
		parts.put("rightShoulder", rightShoulder);
		parts.put("rightArm1", rightArm1);
		parts.put("rightArm2", rightArm2);
		parts.put("rightArm3", rightArm3);
		
		parts.put("leftShoulder", leftShoulder);
		parts.put("leftArm1", leftArm1);
		parts.put("leftArm2", leftArm2);
		parts.put("leftArm3", leftArm3);
		
		parts.put("leftHip", leftHip);
		parts.put("leftLeg1", leftLeg1);
		parts.put("leftLeg2", leftLeg2);
		parts.put("leftLeg3", leftLeg3);
		
		parts.put("rightHip", rightHip);
		parts.put("rightLeg1", rightLeg1);
		parts.put("rightLeg2", rightLeg2);
		parts.put("rightLeg3", rightLeg3);
		
		rightArmP = rightShoulder;
		rightArm.add(rightArm1);
		rightArm.add(rightArm2);
		rightArm.add(rightArm3);
		leftArmP = leftShoulder;
		leftArm.add(leftArm1);
		leftArm.add(leftArm2);
		leftArm.add(leftArm3);
		rightLegP = rightHip;
		rightLeg.add(rightLeg1);
		rightLeg.add(rightLeg2);
		rightLeg.add(rightLeg3);
		leftLegP = leftHip;
		leftLeg.add(leftLeg1);
		leftLeg.add(leftLeg2);
		leftLeg.add(leftLeg3);
	}

	@Override
	public void stopWalk() {
		currentAnimation.cancel();
	}
	
	public void startWalk() {
		currentAnimation = new BukkitRunnable() {
			double angle = 270;
			boolean forward = true;
			public void run() {
				for(int i = 0; i < rightArm.size(); i++) {
					double rad = (0.5 + (i * 0.4));
					
					double forwardBack = (rad * Math.cos(Math.toRadians(angle)));
					double upDown = ((1.5 - (i * 0.4))+rad) + (rad * Math.sin(Math.toRadians(angle)));
					rightArm.get(i).setRelativeLocation(new RelativeLocation(upDown, rightArm.get(i).getRelative().getLeftRight(), forwardBack));
					rightArm.get(i).setHeadRotation(new EulerAngle(Math.toRadians(270-angle), 0, 0));
					leftArm.get(i).setRelativeLocation(new RelativeLocation(upDown, leftArm.get(i).getRelative().getLeftRight(), -forwardBack));
					leftArm.get(i).setHeadRotation(new EulerAngle(-Math.toRadians(270-angle), 0, 0));
				}
				
				for(int i = 0; i < rightLeg.size(); i++) {
					double rad = (0.5 + (i * 0.4));
					double forwardBack = (rad * Math.cos(Math.toRadians(angle)));
					double upDown = ((0.1 - (i * 0.4))+rad) + (rad * Math.sin(Math.toRadians(angle)));
					rightLeg.get(i).setRelativeLocation(new RelativeLocation(upDown, rightLeg.get(i).getRelative().getLeftRight(), -forwardBack));
					rightLeg.get(i).setHeadRotation(new EulerAngle(-Math.toRadians(270-angle), 0, 0));
					leftLeg.get(i).setRelativeLocation(new RelativeLocation(upDown, leftLeg.get(i).getRelative().getLeftRight(), forwardBack));
					leftLeg.get(i).setHeadRotation(new EulerAngle(Math.toRadians(270-angle), 0, 0));
				}
				rightArmP.setHeadRotation(new EulerAngle(Math.toRadians(270-angle), 0, 0));
				leftArmP.setHeadRotation(new EulerAngle(-Math.toRadians(270-angle), 0, 0));
				rightLegP.setHeadRotation(new EulerAngle(-Math.toRadians(270-angle), 0, 0));
				leftLegP.setHeadRotation(new EulerAngle(Math.toRadians(270-angle), 0, 0));
				if(forward) {
					angle += 5;
					if(angle == 315) forward = false;
				} else {
					angle -= 5;
					if(angle == 225) forward = true;
				}
			}
		};
		currentAnimation.runTaskTimer(MinecraftMMO.getInstance(), 0, 1);
	}
	
	private enum MagmaBossParts {
		HEART(new ItemStack(Material.REDSTONE_BLOCK)),
		HEAD(SkullCreator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTFkZmQ1NjIzM2U5OWQ2MzAyZDU4YjU3ZWZlMzRkYWU1NzEwY2EyMzQ3NzA2ZGI4YmM4ZDI1YjdlM2RhZjQ2MiJ9fX0=")),
		SHOULDER(SkullCreator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY3ZDNkYTZhOWM2ZjRjYTFmNDgwNjJmNmY0ZDljM2YzZTJjZjEzM2U4Yzg1MjhiODc0ZGQ4Nzk2MjZhOTY3NiJ9fX0=")),
		BODY(SkullCreator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI5NTk4MjJhMTAxNmJiNDdiZmNkODJiZmE0NTYwMzA4MzJiZDY2YmExNWM3OTczOTg1M2IwZDE5ZDBkNmViNyJ9fX0=")),
		ARM(SkullCreator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDQ5OGY4MjA5NjhiNWRmMjcyYmEzMWE2ZDhlMmI0MjA2Yjk1OTY3ZWI4MjhjOGM1OWY0YmYxMmUyMzZlZiJ9fX0="));
		
		@Getter
		private ItemStack stack;
		
		MagmaBossParts(ItemStack itemStack) {
			stack = itemStack;
		}
	}
	
}