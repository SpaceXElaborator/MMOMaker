package com.terturl.MMO.Util.Items;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;

import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;
import com.terturl.MMO.Util.Items.ItemEnums.MMOModifiers;
import com.terturl.MMO.Util.Items.ItemEnums.Rarity;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;
import com.terturl.MMO.Util.Strings.StringUtils;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class CustomItemManager {

	@Getter
	private final Map<String, CustomItem> customItems = new HashMap<String, CustomItem>();

	@Getter
	private File warnings;

	@Getter
	private final Material[] boots = new Material[] { Material.LEATHER_BOOTS, Material.IRON_BOOTS,
			Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.CHAINMAIL_BOOTS, Material.NETHERITE_BOOTS };
	@Getter
	private final Material[] chests = new Material[] { Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE,
			Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
			Material.NETHERITE_CHESTPLATE };
	@Getter
	private final Material[] pants = new Material[] { Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS,
			Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.CHAINMAIL_LEGGINGS,
			Material.NETHERITE_LEGGINGS };
	@Getter
	private final Material[] hats = new Material[] { Material.LEATHER_HELMET, Material.IRON_HELMET,
			Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.CHAINMAIL_HELMET, Material.NETHERITE_HELMET };
	@Getter
	private final Material[] leather = new Material[] { Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE,
			Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET };

	public CustomItemManager() throws IOException {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[MMO-RPG] Registering Items...");
		customItems.put("MMO_ITEM_EMPTY_SLOT_ITEM",
				new CustomItem("MMO_ITEM_EMPTY_SLOT_ITEM", Material.AIR, 0, Rarity.COMMON, CraftRarity.CRUDE));

		List<Material> armor = new ArrayList<>();
		for (Material m : boots) {
			armor.add(m);
		}
		for (Material m : chests) {
			armor.add(m);
		}
		for (Material m : pants) {
			armor.add(m);
		}
		for (Material m : hats) {
			armor.add(m);
		}

		File mainItemDir = new File(MinecraftMMO.getInstance().getDataFolder(), "items");
		if (!mainItemDir.exists())
			mainItemDir.mkdir();
		warnings = new File(mainItemDir, "Warnings.txt");
		File armors = new File(mainItemDir, "armor");
		File items = new File(mainItemDir, "items");
		File weapons = new File(mainItemDir, "weapons");
		if (!armors.exists())
			armors.mkdir();
		if (!items.exists())
			items.mkdir();
		if (!weapons.exists())
			weapons.mkdir();

		if (getWarnings().exists()) {
			getWarnings().delete();
		}

		try {
			getWarnings().createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (items.listFiles().length != 0) {
			for (File f : items.listFiles()) {
				if (f.getName().endsWith(".json")) {
					if (checkItemGeneric(f)) {
						JsonObject config = new JsonFileInterpretter(f).getJson();
						String name = config.get("Name").getAsString();
						String friendlyname = config.has("FriendlyName") ? config.get("FriendlyName").getAsString() : name;
						int itemModelData = config.has("ItemModelData") ? config.get("ItemModelData").getAsInt() : 0;
						boolean canCraft = config.has("CanCraft") ? config.get("CanCraft").getAsBoolean() : false;
						boolean craftOnly = config.has("CraftOnly") ? config.get("CraftOnly").getAsBoolean() : false;
						List<String> lore = new ArrayList<>();
						if(config.has("Lore") && config.get("Lore").isJsonArray()) {
							config.get("Lore").getAsJsonArray().forEach(e -> {
								lore.add(e.getAsString());
							});
						}
						
						boolean soulBound = config.has("SoulBound") ? config.get("SoulBound").getAsBoolean() : false;
						Material mat = Material.getMaterial(config.get("Item").getAsString().toUpperCase());
						Rarity rare = config.has("Rarity")
								? Rarity.valueOf(config.get("Rarity").getAsString().toUpperCase())
								: Rarity.COMMON;
						CraftRarity craftRarity = config.has("CraftRarity")
								? CraftRarity.valueOf(config.get("CraftRarity").getAsString().toUpperCase())
								: CraftRarity.CRUDE;
						CustomItem ci = new CustomItem(name, mat, itemModelData, rare, craftRarity);
						ci.setFriendlyName(friendlyname);
						ci.setCraftable(canCraft);
						ci.setCraftOnly(craftOnly);
						ci.setLore(lore);
						ci.setSoulBound(soulBound);
						customItems.put(name, ci);
					}
				}
			}
		}

		if (weapons.listFiles().length != 0) {
			for (File f : weapons.listFiles()) {
				if (f.getName().endsWith(".json")) {
					if(checkItemGeneric(f)) {
						if(checkWeapon(f)) {
							JsonObject config = new JsonFileInterpretter(f).getJson();
							String name = config.get("Name").getAsString();
							String friendlyname = config.has("FriendlyName") ? config.get("FriendlyName").getAsString()
									: name.replaceAll("_", " ");
							Material mat = Material.getMaterial(config.get("Item").getAsString().toUpperCase());
							int customItemModel = config.has("CustomItemModel") ? config.get("CustomItemModel").getAsInt() : 0;
							Rarity rare = config.has("Rarity")
									? Rarity.valueOf(config.get("Rarity").getAsString().toUpperCase())
									: Rarity.COMMON;
							CraftRarity craftRarity = config.has("CraftRarity")
									? CraftRarity.valueOf(config.get("CraftRarity").getAsString().toUpperCase())
									: CraftRarity.CRUDE;
							SlotType st = config.has("SlotType") ? SlotType.valueOf(config.get("SlotType").getAsString().toUpperCase()) : SlotType.MAIN_HAND;
							boolean canCraft = config.has("CanCraft") ? config.get("CanCraft").getAsBoolean() : false;
							boolean craftOnly = config.has("CraftOnly") ? config.get("CraftOnly").getAsBoolean() : false;
							int itemLevel = config.has("Level") ? config.get("Level").getAsInt() : 1;
							double itemDurability = config.has("ItemDurability")
									? config.get("ItemDurability").getAsDouble()
									: 0.0;
							double itemMaxDurability = config.has("ItemMaxDurability")
									? config.get("ItemMaxDurability").getAsDouble()
									: 0.0;
							List<String> lore = new ArrayList<>();
							if(config.has("Lore") && config.get("Lore").isJsonArray()) {
								config.get("Lore").getAsJsonArray().forEach(e -> {
									lore.add(e.getAsString());
								});
							}
							Map<MMOModifiers, Object> mods = (config.has("Modifiers") && (config.get("Modifiers").isJsonObject())) ? getModifiers(config.get("Modifiers").getAsJsonObject()) : new HashMap<MMOModifiers, Object>();
							boolean soulBound = config.has("SoulBound") ? config.get("SoulBound").getAsBoolean() : false;
							boolean ranged = config.has("Ranged") ? config.get("Ranged").getAsBoolean() : false;
							CustomWeapon cw = new CustomWeapon(name, mat, customItemModel, itemLevel, rare, craftRarity, st, ranged);
							cw.setFriendlyName(friendlyname);
							cw.setCraftable(canCraft);
							cw.setCraftOnly(craftOnly);
							cw.setDurability(itemDurability);
							cw.setMaxDurability(itemMaxDurability);
							cw.setLore(lore);
							cw.setSoulBound(soulBound);
							cw.setMods(mods);
							customItems.put(name, cw);
						}
					}
				}
			}
		}
		
		if (armors.listFiles().length != 0) {
			for (File f : armors.listFiles()) {
				if (f.getName().endsWith(".json")) {
					if (checkItemGeneric(f)) {
						if (checkArmor(f, armor)) {
							JsonObject config = new JsonFileInterpretter(f).getJson();
							String name = config.get("Name").getAsString();
							String friendlyname = config.has("FriendlyName") ? config.get("FriendlyName").getAsString()
									: name;
							Material mat = Material.getMaterial(config.get("Item").getAsString().toUpperCase());
							int customItemModel = config.has("CustomItemModel") ? config.get("CustomItemModel").getAsInt() : 0;
							Rarity rare = config.has("Rarity")
									? Rarity.valueOf(config.get("Rarity").getAsString().toUpperCase())
									: Rarity.COMMON;
							CraftRarity craftRarity = config.has("CraftRarity")
									? CraftRarity.valueOf(config.get("CraftRarity").getAsString().toUpperCase())
									: CraftRarity.CRUDE;
							SlotType st = SlotType.valueOf(config.get("SlotType").getAsString().toUpperCase());
							boolean canCraft = config.has("CanCraft") ? config.get("CanCraft").getAsBoolean() : false;
							boolean craftOnly = config.has("CraftOnly") ? config.get("CraftOnly").getAsBoolean() : false;
							int itemLevel = config.has("Level") ? config.get("Level").getAsInt() : 1;
							double itemDurability = config.has("ItemDurability")
									? config.get("ItemDurability").getAsDouble()
									: 0.0;
							double itemMaxDurability = config.has("ItemMaxDurability")
									? config.get("ItemMaxDurability").getAsDouble()
									: 0.0;
							List<String> lore = new ArrayList<>();
							if(config.has("Lore") && config.get("Lore").isJsonArray()) {
								config.get("Lore").getAsJsonArray().forEach(e -> {
									lore.add(e.getAsString());
								});
							}
							boolean soulBound = config.has("SoulBound") ? config.get("SoulBound").getAsBoolean() : false;
							Color c = config.has("Color")
									? StringUtils.getColorFromString(config.get("Color").getAsString())
									: null;
							Map<MMOModifiers, Object> mods = (config.has("Modifiers") && (config.get("Modifiers").isJsonObject())) ? getModifiers(config.get("Modifiers").getAsJsonObject()) : new HashMap<MMOModifiers, Object>();
							CustomArmor ci = new CustomArmor(name, mat, customItemModel, itemLevel, rare, craftRarity, st);
							ci.setFriendlyName(friendlyname);
							ci.setCraftable(canCraft);
							ci.setCraftOnly(craftOnly);
							ci.setDurability(itemDurability);
							ci.setMaxDurability(itemMaxDurability);
							ci.setLore(lore);
							ci.setSoulBound(soulBound);
							ci.setMods(mods);
							if (c != null) {
								ci.setColor(c);
							}
							customItems.put(name, ci);
						}
					}
				}
			}
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MMO-RPG] Done");
	}

	public CustomItem getItem(String name) {
		if (customItems.containsKey(name)) {
			return customItems.get(name);
		}
		return null;
	}
	
	private Map<MMOModifiers, Object> getModifiers(JsonObject config) {
		Map<MMOModifiers, Object> mapping = new HashMap<MMOModifiers, Object>();
		
		if(config.has("Health")) {
			mapping.put(MMOModifiers.HEALTH, config.get("Health").getAsDouble());
		}
		
		if(config.has("Damage")) {
			mapping.put(MMOModifiers.DAMAGE, config.get("Damage").getAsDouble());
		}
		
		if(config.has("Armor")) {
			mapping.put(MMOModifiers.DEFENSE, config.get("Armor").getAsDouble());
		}
		
		return mapping;
	}
	
	private boolean checkWeapon(File f) throws IOException {
		boolean load = true;
		
		JsonObject config = new JsonFileInterpretter(f).getJson();
		
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		if(config.has("SlotType")) {
			SlotType slotType = SlotType.valueOf(config.get("SlotType").getAsString().toUpperCase());
			if(slotType != SlotType.OFF_HAND || slotType != SlotType.MAIN_HAND) {
				wbw.write("[" + f.getName() + "] 'SlotType' Must be Off_Hand or Main_Hand!");
				wbw.newLine();
				load = false;
			}
		}
		
		wbw.flush();
		wbw.close();
		return load;
	}
	
	private boolean checkArmor(File f, List<Material> armor) throws IOException {
		boolean load = true;
		boolean wrongSlot = false;
		JsonObject config = new JsonFileInterpretter(f).getJson();

		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);

		Material mat = Material.valueOf(config.get("Item").getAsString().toUpperCase());

		if (config.has("SlotType")) {
			SlotType slotType = SlotType.valueOf(config.get("SlotType").getAsString().toUpperCase());

			if (config.has("Color")) {
				boolean isLeather = false;
				for (Material leth : leather) {
					if (mat.equals(leth)) {
						isLeather = true;
					}
				}
				if (!isLeather) {
					wbw.write("[" + f.getName() + "] 'Color' property can only be used on leather armor!");
					wbw.newLine();
					load = false;
				}
			}

			if (load) {
				for (Material m : armor) {
					if (mat.equals(m)) {
						if (slotType.equals(SlotType.BOOTS)) {
							for (Material m2 : boots) {
								if (mat.equals(m2)) {
									load = true;
									break;
								} else {
									load = false;
									wrongSlot = true;
								}
							}
						}
						if (slotType.equals(SlotType.CHEST)) {
							for (Material m2 : chests) {
								if (mat.equals(m2)) {
									load = true;
									break;
								} else {
									load = false;
									wrongSlot = true;
								}
							}
						}
						if (slotType.equals(SlotType.LEGS)) {
							for (Material m2 : pants) {
								if (mat.equals(m2)) {
									load = true;
									break;
								} else {
									load = false;
									wrongSlot = true;
								}
							}
						}
						if (slotType.equals(SlotType.HELMET)) {
							for (Material m2 : hats) {
								if (mat.equals(m2)) {
									load = true;
									break;
								} else {
									load = false;
									wrongSlot = true;
								}
							}
						}
					}
				}
			}
		} else {
			wbw.write("[" + f.getName() + "] 'SlotType' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (wrongSlot) {
			wbw.write("[" + f.getName() + "] 'SlotType' does not match the armor piece!");
			wbw.newLine();
			load = false;
		}

		wbw.flush();
		wbw.close();

		return load;
	}

	private boolean checkItemGeneric(File f) throws IOException {
		boolean load = true;

		JsonObject config = new JsonFileInterpretter(f).getJson();

		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);

		if (!config.has("Name")) {
			wbw.write("[" + f.getName() + "] 'name' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (!config.has("Item")) {
			wbw.write("[" + f.getName() + "] 'Item' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (config.has("CustomItemModel")) {
			Integer damage = StringUtils.isInt(config.get("CustomItemModel").getAsString()) ? config.get("CustomItemModel").getAsInt()
					: null;
			if (damage == null) {
				wbw.write("[" + f.getName() + "] 'Durability' MUST be a number!");
				wbw.newLine();
				load = false;
			}
		}

		if (config.has("Level")) {
			Integer level = StringUtils.isInt(config.get("Level").getAsString()) ? config.get("Level").getAsInt() : null;
			if (level == null) {
				wbw.write("[" + f.getName() + "] 'Level' MUST be a number!");
				wbw.newLine();
				load = false;
			}
		}

		if (config.has("Rarity")) {
			String s = Rarity.exists(config.get("Rarity").getAsString().toUpperCase()) ? config.get("Rarity").getAsString().toUpperCase() : null;
			if (s == null) {
				wbw.write("[" + f.getName() + "] Rarity: " + config.get("Rarity").getAsString() + " does not exist!");
				wbw.newLine();
				load = false;
			}
		}
		
		if (config.has("CraftRarity")) {
			String s = CraftRarity.exists(config.get("CraftRarity").getAsString().toUpperCase()) ? config.get("CraftRarity").getAsString().toUpperCase() : null;
			if (s == null) {
				wbw.write("[" + f.getName() + "] CraftRarity: " + config.get("CraftRarity").getAsString() + " does not exist!");
				wbw.newLine();
				load = false;
			}
		}

		if (config.has("SlotType")) {
			String s = SlotType.exists(config.get("SlotType").getAsString().toUpperCase()) ? config.get("SlotType").getAsString().toUpperCase()
					: null;
			if (s == null) {
				wbw.write("[" + f.getName() + "] Slot Type: " + config.get("SlotType").getAsString() + " does not exist!");
				wbw.newLine();
				load = false;
			}
		}

		wbw.flush();
		wbw.close();

		return load;
	}

}