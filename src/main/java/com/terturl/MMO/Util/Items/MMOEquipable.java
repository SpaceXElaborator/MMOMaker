package com.terturl.MMO.Util.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;
import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;
import com.terturl.MMO.Util.Items.ItemEnums.MMOModifiers;
import com.terturl.MMO.Util.Items.ItemEnums.Rarity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;

public class MMOEquipable extends CustomItem {

	@Getter @Setter
	private List<MMORunes> runes;
	@Getter
	@Setter
	private int itemLevel = 1;
	@Getter
	@Setter
	private double durability = 50.0;
	@Getter
	@Setter
	private double maxDurability = 50.0;
	@Getter
	@Setter
	private Map<MMOModifiers, Object> mods = new HashMap<>();
	@Getter
	@Setter
	private Map<MMOModifiers, Double> modsOn = new HashMap<>();
	@Getter
	@Setter
	private Map<MMOModifiers, Double> valueAddOn = new HashMap<>();
	
	public MMOEquipable(String name, Material mat, Integer itemD, Rarity rare, CraftRarity cr, Integer level) {
		super(name, mat, itemD, rare, cr);
		setItemLevel(level);
	}
	
	@Override
	public ItemStack makeItem(Integer amt) {
		ItemStack i = new ItemStack(getItemMat(), amt);
		ItemMeta m = i.getItemMeta();
		if(m != null) {
			m.setDisplayName(getRarity().getChatColor() + getName().replaceAll("_", " "));
			m.setUnbreakable(true);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + "Level: " + String.valueOf(getItemLevel()));
			if(getMods().size() > 0) {
				lore.add("");
			}
			for (MMOModifiers mods : getMods().keySet()) {
				Object o = getMods().get(mods);
				double d = 0.0;
				if (o instanceof Double) {
					d = (double) getMods().get(mods);
					getModsOn().put(mods, d);
				} else if (o instanceof String) {
					String s = (String) getMods().get(mods);
					if(s.contains(",")) {
						int in = getRandom(s);
						d = in + 0.0D;
						getModsOn().put(mods, d);
					} else {
						d = Double.valueOf(s);
						getModsOn().put(mods, d);
					}
				}
				lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getModsOn().get(mods));
				
//				if (getCraftingRarity() == null) {
//					Object o = getMods().get(mods);
//					Double mult = null;
//					Double d = null;
//					if (o instanceof Double) {
//						d = (Double) getMods().get(mods);
//						getModsOn().put(mods, d);
//					} else if (o instanceof String) {
//						String s = (String) getMods().get(mods);
//						Integer in = getRandom(s);
//						d = in + 0.0D;
//						getModsOn().put(mods, d);
//					}
//					mult = d * getCraftingRarity().getMultiplier();
//					DecimalFormat df = new DecimalFormat("##.00");
//					getValueAddOn().put(mods, mult);
//					lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getModsOn().get(mods)
//							+ " " + ChatColor.GOLD + "+" + df.format(mult));
//				}
//	
//				if (getCraftingRarity().equals(CraftRarity.CRUDE)) {
//					lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getMods().get(mods));
//				} else {
//					Object o = getMods().get(mods);
//					Double mult = null;
//					Double d = null;
//					if (o instanceof Double) {
//						d = (Double) getMods().get(mods);
//						getModsOn().put(mods, d);
//					} else if (o instanceof String) {
//						String s = (String) getMods().get(mods);
//						Integer in = getRandom(s);
//						d = in + 0.0D;
//						getModsOn().put(mods, d);
//					}
//					mult = d * getCraftingRarity().getMultiplier();
//					DecimalFormat df = new DecimalFormat("##.00");
//					getValueAddOn().put(mods, mult);
//					lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getModsOn().get(mods)
//							+ " " + ChatColor.GOLD + "+" + df.format(mult));
//				}
			}
			
			if(getLore().size() > 0) {
				lore.add("");
			}
			
			for(String s : getLore()) {
				lore.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			
			if (!getMadeBy().isEmpty()) {
				lore.add("");
				lore.add(getCraftingRarity().getChatColor() + getCraftingRarity().getFriendlyName());
				lore.add(ChatColor.GRAY + "Creator: " + ChatColor.DARK_GRAY + getMadeBy());
			}
			
			if (isSoulBound()) {
				lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Soul-Bound");
			}
			
			if(getDurability() != 0.0 && getMaxDurability() != 0.0) {
				lore.add("");
				lore.add(ChatColor.GRAY + "Durability: " + String.valueOf(getDurability()) + "/"
						+ String.valueOf(getMaxDurability()));
			}
			m.setLore(lore);
			m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
			for (Attribute a : Attribute.values()) {
				m.removeAttributeModifier(a);
			}
			m.setCustomModelData(getCustomItemModel());
			i.setItemMeta(m);
		}
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(i);
		NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
		tag.setBoolean("CustomItem", true);
		Gson g = new Gson();
		tag.setString("CustomItemValues", g.toJson(this));
		stack.setTag(tag);
		ItemStack after = CraftItemStack.asBukkitCopy(stack);
		return after;
	}
	
	@Override
	public ItemStack makeItem() {
		return makeItem(1);
	}
	
}