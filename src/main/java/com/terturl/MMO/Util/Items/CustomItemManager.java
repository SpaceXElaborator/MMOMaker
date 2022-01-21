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

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;
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
						JsonFileInterpretter config = new JsonFileInterpretter(f);
						String name = config.getString("Name");
						String friendlyname = config.contains("FriendlyName") ? config.getString("FriendlyName") : name;
						Integer itemModelData = config.contains("ItemModelData") ? config.getInt("ItemModelData") : 0;
						Boolean canCraft = config.contains("CanCraft") ? config.getBoolean("CanCraft") : false;
						Boolean craftOnly = config.contains("CraftOnly") ? config.getBoolean("CraftOnly") : false;
						List<String> lore = config.contains("Lore") ? config.getStringList("Lore") : new ArrayList<>();
						Boolean soulBound = config.contains("SoulBound") ? config.getBoolean("SoulBound") : false;
						Material mat = Material.getMaterial(config.getString("Item").toUpperCase());
						Rarity rare = config.contains("Rarity")
								? Rarity.valueOf(config.getString("Rarity").toUpperCase())
								: Rarity.COMMON;
						CraftRarity craftRarity = config.contains("CraftRarity")
								? CraftRarity.valueOf(config.getString("CraftRarity").toUpperCase())
								: CraftRarity.CRUDE;
						CustomItem ci = new CustomItem(name, mat, itemModelData, rare, craftRarity);
						ci.setFriendlyName(friendlyname);
						ci.setCanCraft(canCraft);
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
							JsonFileInterpretter config = new JsonFileInterpretter(f);
							String name = config.getString("Name");
							String friendlyname = config.contains("FriendlyName") ? config.getString("FriendlyName")
									: name.replaceAll("_", " ");
							Material mat = Material.getMaterial(config.getString("Item").toUpperCase());
							Integer customItemModel = config.contains("CustomItemModel") ? config.getInt("CustomItemModel") : 0;
							Rarity rare = config.contains("Rarity")
									? Rarity.valueOf(config.getString("Rarity").toUpperCase())
									: Rarity.COMMON;
							CraftRarity craftRarity = config.contains("CraftRarity")
									? CraftRarity.valueOf(config.getString("CraftRarity").toUpperCase())
									: CraftRarity.CRUDE;
							SlotType st = config.contains("SlotType") ? SlotType.valueOf(config.getString("SlotType").toUpperCase()) : SlotType.MAIN_HAND;
							Boolean canCraft = config.contains("CanCraft") ? config.getBoolean("CanCraft") : false;
							Boolean craftOnly = config.contains("CraftOnly") ? config.getBoolean("CraftOnly") : false;
							Integer itemLevel = config.contains("Level") ? config.getInt("Level") : 1;
							Double itemDurability = config.contains("ItemDurability")
									? config.getDouble("ItemDurability")
									: 0.0;
							Double itemMaxDurability = config.contains("ItemMaxDurability")
									? config.getDouble("ItemMaxDurability")
									: 0.0;
							List<String> lore = config.contains("Lore") ? config.getStringList("Lore")
									: new ArrayList<>();
							Boolean soulBound = config.contains("SoulBound") ? config.getBoolean("SoulBound") : false;
							Boolean ranged = config.contains("Ranged") ? config.getBoolean("Ranged") : false;
							CustomWeapon cw = new CustomWeapon(name, mat, customItemModel, itemLevel, rare, craftRarity, st, ranged);
							cw.setFriendlyName(friendlyname);
							cw.setCanCraft(canCraft);
							cw.setCraftOnly(craftOnly);
							cw.setDurability(itemDurability);
							cw.setMaxDurability(itemMaxDurability);
							cw.setLore(lore);
							cw.setSoulBound(soulBound);
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
							JsonFileInterpretter config = new JsonFileInterpretter(f);
							String name = config.getString("Name");
							String friendlyname = config.contains("FriendlyName") ? config.getString("FriendlyName")
									: name;
							Material mat = Material.getMaterial(config.getString("Item").toUpperCase());
							Integer customItemModel = config.contains("CustomItemModel") ? config.getInt("CustomItemModel") : 0;
							Rarity rare = config.contains("Rarity")
									? Rarity.valueOf(config.getString("Rarity").toUpperCase())
									: Rarity.COMMON;
							CraftRarity craftRarity = config.contains("CraftRarity")
									? CraftRarity.valueOf(config.getString("CraftRarity").toUpperCase())
									: CraftRarity.CRUDE;
							SlotType st = SlotType.valueOf(config.getString("SlotType").toUpperCase());
							Boolean canCraft = config.contains("CanCraft") ? config.getBoolean("CanCraft") : false;
							Boolean craftOnly = config.contains("CraftOnly") ? config.getBoolean("CraftOnly") : false;
							Integer itemLevel = config.contains("Level") ? config.getInt("Level") : 1;
							Double itemDurability = config.contains("ItemDurability")
									? config.getDouble("ItemDurability")
									: 0.0;
							Double itemMaxDurability = config.contains("ItemMaxDurability")
									? config.getDouble("ItemMaxDurability")
									: 0.0;
							List<String> lore = config.contains("Lore") ? config.getStringList("Lore")
									: new ArrayList<>();
							Boolean soulBound = config.contains("SoulBound") ? config.getBoolean("SoulBound") : false;
							Color c = config.contains("Color")
									? StringUtils.getColorFromString(config.getString("Color"))
									: null;
							CustomArmor ci = new CustomArmor(name, mat, customItemModel, itemLevel, rare, craftRarity, st);
							ci.setFriendlyName(friendlyname);
							ci.setCanCraft(canCraft);
							ci.setCraftOnly(craftOnly);
							ci.setDurability(itemDurability);
							ci.setMaxDurability(itemMaxDurability);
							ci.setLore(lore);
							ci.setSoulBound(soulBound);
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
	
	private boolean checkWeapon(File f) throws IOException {
		boolean load = true;
		
		JsonFileInterpretter config = new JsonFileInterpretter(f);
		
		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);
		
		if(config.contains("SlotType")) {
			SlotType slotType = SlotType.valueOf(config.getString("SlotType").toUpperCase());
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
		JsonFileInterpretter config = new JsonFileInterpretter(f);

		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);

		Material mat = Material.valueOf(config.getString("Item").toUpperCase());

		if (config.contains("SlotType")) {
			SlotType slotType = SlotType.valueOf(config.getString("SlotType").toUpperCase());

			if (config.contains("Color")) {
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

		JsonFileInterpretter config = new JsonFileInterpretter(f);

		FileWriter wfw = new FileWriter(getWarnings());
		BufferedWriter wbw = new BufferedWriter(wfw);

		if (!config.contains("Name")) {
			wbw.write("[" + f.getName() + "] 'name' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (!config.contains("Item")) {
			wbw.write("[" + f.getName() + "] 'Item' property is not set!");
			wbw.newLine();
			load = false;
		}

		if (config.contains("CustomItemModel")) {
			Integer damage = StringUtils.isInt(config.getString("CustomItemModel")) ? config.getInt("CustomItemModel")
					: null;
			if (damage == null) {
				wbw.write("[" + f.getName() + "] 'Durability' MUST be a number!");
				wbw.newLine();
				load = false;
			}
		}

		if (config.contains("Level")) {
			Integer level = StringUtils.isInt(config.getString("Level")) ? config.getInt("Level") : null;
			if (level == null) {
				wbw.write("[" + f.getName() + "] 'Level' MUST be a number!");
				wbw.newLine();
				load = false;
			}
		}

		if (config.contains("Rarity")) {
			String s = Rarity.exists(config.getString("Rarity")) ? config.getString("Rarity").toUpperCase() : null;
			if (s == null) {
				wbw.write("[" + f.getName() + "] Rarity: " + config.getString("Rarity") + " does not exist!");
				wbw.newLine();
				load = false;
			}
		}
		
		if (config.contains("CraftRarity")) {
			String s = CraftRarity.exists(config.getString("CraftRarity")) ? config.getString("CraftRarity").toUpperCase() : null;
			if (s == null) {
				wbw.write("[" + f.getName() + "] CraftRarity: " + config.getString("CraftRarity") + " does not exist!");
				wbw.newLine();
				load = false;
			}
		}

		if (config.contains("SlotType")) {
			String s = SlotType.exists(config.getString("SlotType")) ? config.getString("SlotType").toUpperCase()
					: null;
			if (s == null) {
				wbw.write("[" + f.getName() + "] Slot Type: " + config.getString("SlotType") + " does not exist!");
				wbw.newLine();
				load = false;
			}
		}

		wbw.flush();
		wbw.close();

		return load;
	}

}