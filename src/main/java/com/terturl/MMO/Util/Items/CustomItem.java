package com.terturl.MMO.Util.Items;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;

@EqualsAndHashCode
public class CustomItem {

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
	private SlotType slotType = SlotType.TRINKET;
	@Getter
	@Setter
	private Integer itemLevel = 1;
	@Getter
	@Setter
	private Double durability = 50.0;
	@Getter
	@Setter
	private Double maxDurability = 50.0;
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
	@Getter
	@Setter
	private HashMap<Modifiers, Object> mods = new HashMap<>();
	@Getter
	@Setter
	private HashMap<Modifiers, Double> modsOn = new HashMap<>();
	@Getter
	@Setter
	private HashMap<Modifiers, Double> valueAddOn = new HashMap<>();

	public CustomItem(String name, Material mat, Integer itemD, Integer level, Rarity rare) {
		setName(name);
		setItemMat(mat);
		setCustomItemModel(itemD);
		setItemLevel(level);
		setRarity(rare);
	}
	
	public CustomItem(Player p, String name, Material mat, Integer itemD, Integer level, Rarity rare, CraftRarity cr) {
		this(name, mat, itemD, level, rare);
		setCraftingRarity(cr);
		madeBy = p.getName();
	}
	
	public ItemStack makeItem(Integer amt) {
		ItemStack i = new ItemStack(getItemMat(), amt);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(getRarity().getChatColor() + getName().replaceAll("_", " "));
		m.setUnbreakable(true);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "Level: " + String.valueOf(getItemLevel()));
		if(getMods().size() > 1) {
			lore.add("");
		}
		for (Modifiers mods : getMods().keySet()) {
			if (getCraftingRarity() == null) {
				Object o = getMods().get(mods);
				Double mult = null;
				Double d = null;
				if (o instanceof Double) {
					d = (Double) getMods().get(mods);
					getModsOn().put(mods, d);
				} else if (o instanceof String) {
					String s = (String) getMods().get(mods);
					Integer in = getRandom(s);
					d = in + 0.0D;
					getModsOn().put(mods, d);
				}
				mult = d * getCraftingRarity().getMultiplier();
				DecimalFormat df = new DecimalFormat("##.00");
				getValueAddOn().put(mods, mult);
				lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getModsOn().get(mods)
						+ " " + ChatColor.GOLD + "+" + df.format(mult));
			}

			if (getCraftingRarity().equals(CraftRarity.CRUDE)) {
				lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getMods().get(mods));
			} else {
				Object o = getMods().get(mods);
				Double mult = null;
				Double d = null;
				if (o instanceof Double) {
					d = (Double) getMods().get(mods);
					getModsOn().put(mods, d);
				} else if (o instanceof String) {
					String s = (String) getMods().get(mods);
					Integer in = getRandom(s);
					d = in + 0.0D;
					getModsOn().put(mods, d);
				}
				mult = d * getCraftingRarity().getMultiplier();
				DecimalFormat df = new DecimalFormat("##.00");
				getValueAddOn().put(mods, mult);
				lore.add(ChatColor.GRAY + mods.getFriendlyName() + ":" + ChatColor.GREEN + " +" + getModsOn().get(mods)
						+ " " + ChatColor.GOLD + "+" + df.format(mult));
			}
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
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(i);
		NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
		tag.setString("CustomItem", "true");
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

	public enum Modifiers {
		HEALTH("Health"), DAMAGE("Damage"), LOOT("Loot"), SPEED("Speed"), DEFENSE("Defense"), MANA("Mana"),
		RAGE("Rage");

		Modifiers(String s) {
			this.n = s;
		}

		private final String n;

		public String getFriendlyName() {
			return n;
		}

		public static boolean exists(String s) {

			for (Modifiers v : values()) {
				if (s.equalsIgnoreCase(v.toString())) {
					return true;
				}
			}

			return false;
		}
	}

	public enum CraftRarity {
		CRUDE(ChatColor.DARK_GRAY, "Crude", 0.0), WELL_MADE(ChatColor.GREEN, "Well-Made", 0.1),
		SUPER_CRAFTED(ChatColor.LIGHT_PURPLE, "Super-Crafted", 0.2),
		MASTER_CRAFTED(ChatColor.YELLOW, "Master-Crafted", 0.3);

		CraftRarity(ChatColor g, String s, Double d) {
			this.g = g;
			this.s = s;
			this.d = d;
		}

		private final ChatColor g;
		private final String s;
		private final Double d;

		public String getFriendlyName() {
			return s;
		}

		public ChatColor getChatColor() {
			return g;
		}

		public Double getMultiplier() {
			return d;
		}

		public static boolean exists(String s) {

			for (CraftRarity v : values()) {
				if (s.equalsIgnoreCase(v.toString())) {
					return true;
				}
			}

			return false;
		}
	}

	public enum Rarity {
		COMMON(ChatColor.DARK_GRAY), UNCOMMON(ChatColor.DARK_GREEN), RARE(ChatColor.DARK_RED),
		LEGENDARY(ChatColor.DARK_PURPLE), MYTHIC(ChatColor.GOLD);

		Rarity(ChatColor g) {
			this.color = g;
		}

		private final ChatColor color;

		public ChatColor getChatColor() {
			return color;
		}

		public static boolean exists(String s) {

			for (Rarity v : values()) {
				if (s.equalsIgnoreCase(v.toString())) {
					return true;
				}
			}

			return false;
		}

	}

	public enum SlotType {
		MAIN_HAND, OFF_HAND, HELMET, CHEST, LEGS, BOOTS, TRINKET, DROP;

		public static boolean exists(String s) {

			for (SlotType v : values()) {
				if (s.equalsIgnoreCase(v.toString())) {
					return true;
				}
			}

			return false;
		}
	}

}