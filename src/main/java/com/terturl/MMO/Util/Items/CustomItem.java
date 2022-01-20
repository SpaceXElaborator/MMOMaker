package com.terturl.MMO.Util.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;
import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;
import com.terturl.MMO.Util.Items.ItemEnums.Rarity;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;

@EqualsAndHashCode
public class CustomItem implements MMOCraftable {

	// TODO: Separation of lowest items upwards.
	// As in, CustomItem should ONLY be name, friendlyName, itemMat, itemModal, Rarity, CraftingRarity, Lore, MadeBy, CanCraft, CraftOnly, SoulBound (Resource Items)
	// StatBoosters: Modifiers (SlotType = Trinket)
	// Damageables: Durability/MaxDurability
	// Armor: (SlotType = Helmet, Chest, Leggings, Boots, OffHand*)
	// Weapons: (SlotType = MainHand, OffHand*)
	
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private String friendlyName = "";
	@Getter
	@Setter
	private Material itemMat;
	@Getter
	@Setter
	private Integer customItemModel = 0;
	@Getter
	@Setter
	private Rarity rarity = Rarity.COMMON;
	@Getter
	@Setter
	private CraftRarity craftingRarity = CraftRarity.CRUDE;
	@Getter
	@Setter
	private List<String> lore = new ArrayList<>();
	@Getter
	@Setter
	private String madeBy = "";
	@Getter
	@Setter
	private String prevOwner = "";
	@Getter
	@Setter
	private boolean soulBound = false;
	@Getter
	@Setter
	private boolean canCraft = false;
	@Getter
	@Setter
	private boolean craftOnly = false;
	
	private Map<CustomItem, Integer> craftingRecipe = new HashMap<CustomItem, Integer>();
	
	private SlotType slotType = SlotType.ITEM;

	public CustomItem(String name, Material mat, Integer itemD, Rarity rare) {
		setName(name);
		setItemMat(mat);
		setCustomItemModel(itemD);
		setRarity(rare);
	}
	
	public CustomItem(Player p, String name, Material mat, Integer itemD, Rarity rare, CraftRarity cr) {
		this(name, mat, itemD, rare);
		setCraftingRarity(cr);
		madeBy = p.getName();
	}
	
	@Override
	public Map<CustomItem, Integer> getCraftingRecipe() {
		return craftingRecipe;
	}

	@Override
	public void setCraftingRecipe(Map<CustomItem, Integer> recipe) {
		craftingRecipe = recipe;
	}
	
	public ItemStack makeItem(Integer amt) {
		ItemStack i = new ItemStack(getItemMat(), amt);
		ItemMeta m = i.getItemMeta();
		if(m != null) {
			m.setDisplayName(getRarity().getChatColor() + getName().replaceAll("_", " "));
			m.setUnbreakable(true);
			List<String> lore = new ArrayList<String>();
			
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
	
	public ItemStack makeItem() {
		return makeItem(1);
	}

	public Integer getRandom(String s) {
		String lat = s.substring(2);
		String[] numbers = lat.split(",");
		int d1 = Integer.valueOf(numbers[0]);
		int d2 = Integer.valueOf(numbers[1]);
		int random = ThreadLocalRandom.current().nextInt(d1, d2 + 1);
		return random;
	}

}