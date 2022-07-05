package com.terturl.MMO.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.NPC.NPC;
import com.terturl.MMO.Entity.NPC.NPC.ItemSlot;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Player.Skills.Crafting.CraftingSkill;
import com.terturl.MMO.Quests.Quest;
import com.terturl.MMO.Util.BasicInventoryItems;
import com.terturl.MMO.Util.JsonFileInterpretter;
import com.terturl.MMO.Util.Items.CustomItemManager;
import com.terturl.MMO.Util.JSONHelpers.ItemUtils;
import com.terturl.MMO.Util.JSONHelpers.LocationUtils;
import com.terturl.MMO.Util.JSONHelpers.StringHelper;

import lombok.Getter;

/**
 * Handles the saving, creating, and loading of new MMOPlayers
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class PlayerHandler {

	@Getter
	private List<MMOPlayer> players = new ArrayList<>();

	@Getter
	private Map<MMOPlayer, List<NPC>> playerNPCs = new HashMap<>();
	private File playersFolder;

	/**
	 * Creates the folders for making MMOPlayer work
	 */
	public PlayerHandler() {
		playersFolder = new File(MinecraftMMO.getInstance().getDataFolder(), "players");
		if (!playersFolder.exists())
			playersFolder.mkdir();
	}

	/**
	 * Get the MMOPlayer that a Projectile belonged to
	 * 
	 * @param p Projectile to check for
	 * @return MMOPlayer or null
	 */
	public MMOPlayer getProjectile(Projectile p) {
		return players.stream().filter(e -> e.getProjectileMapping().containsKey(p.getUniqueId())).findFirst()
				.orElse(null);
	}

	/**
	 * Checks if the MMOPlayer already exists or not
	 * 
	 * @param p Player to search for based on UUID
	 * @return If player Exists or not
	 */
	public boolean PlayerExists(Player p) {
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		if (f.exists())
			return true;
		return false;
	}

	/**
	 * Add a new MMOPlayer to the players list
	 * 
	 * @param mp MMOPlayer to add
	 */
	public void addPlayer(MMOPlayer mp) {
		players.add(mp);
	}

	/**
	 * Removes a Player from the list and deletes them. Perserving file for later
	 * 
	 * @param p Player to delete
	 */
	public void removePlayer(Player p) {
		int removalInt = -1;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerUUID().equals(p.getUniqueId()))
				removalInt = i;
		}
		if (removalInt == -1)
			return;
		players.remove(removalInt);
	}

	/**
	 * Updates the given players inventory with the required items for playing of
	 * the game
	 * 
	 * @param p Player to give items too
	 */
	public void giveBasicItems(Player p) {
		p.getInventory().setItem(8, new ItemStack(Material.COMPASS));
		p.getInventory().setItem(9, BasicInventoryItems.getPlayerClassItem(p));
		p.getInventory().setItem(2, BasicInventoryItems.getQuickActionAbilities());
		p.getInventory().setItem(3, BasicInventoryItems.getQuickActionAbilities());
		p.getInventory().setItem(4, BasicInventoryItems.getQuickActionAbilities());
		p.getInventory().setItem(5, BasicInventoryItems.getRegularAbilities());
		p.getInventory().setItem(6, BasicInventoryItems.getRegularAbilities());
		p.getInventory().setItem(7, BasicInventoryItems.getRegularAbilities());
		p.updateInventory();
	}

	/**
	 * Sets the players current class
	 * 
	 * @param p Player to set their class
	 * @param i Integer of class
	 */
	public void pickClass(Player p, int i) {
		MMOPlayer mp = getPlayer(p);
		mp.setCurrentCharacter(i);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		p.getInventory().setHelmet(cim.getItem(mc.getHelmet()).makeItem());
		p.getInventory().setChestplate(cim.getItem(mc.getChest()).makeItem());
		p.getInventory().setLeggings(cim.getItem(mc.getLegs()).makeItem());
		p.getInventory().setBoots(cim.getItem(mc.getBoots()).makeItem());
		p.getInventory().setItem(0, cim.getItem(mc.getMainH()).makeItem());
		p.getInventory().setItem(1, cim.getItem(mc.getOffH()).makeItem());
		mp.getPlayer().teleport(mc.getClassLocation());
		p.setHealth(mc.getHealth());
		mc.updateStats(p);
		giveBasicItems(p);
		Bukkit.getScheduler().runTaskLater(MinecraftMMO.getInstance(), new Runnable() {
			public void run() {
				mp.updateNPCQuests();
			}
		}, 5);
	}

	/**
	 * Load a player from file
	 * 
	 * @param p Player to load
	 */
	public void loadPlayer(Player p) {
		p.getInventory().clear();
		p.updateInventory();

		// Get the players file based on their UUID
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		MMOPlayer mp = new MMOPlayer(p);
		JsonObject config = new JsonFileInterpretter(f).getJson();

		// Load all of their MMOClasses into their MMOClasses list
		JsonObject jo = config.get("Classes").getAsJsonObject();
		for(Entry<String, JsonElement> entries : jo.entrySet()) {
			JsonObject clazz = entries.getValue().getAsJsonObject();
			int level = clazz.get("Level").getAsInt();
			Location loc = LocationUtils.locationDeSerializer(clazz.get("Location").getAsString());
			double mon = clazz.get("Currency").getAsDouble();
			double xp = clazz.get("XP").getAsDouble();
			double damage = clazz.get("Damage").getAsDouble();
			double defense = clazz.get("Defense").getAsDouble();
			MMOClass mc = (MMOClass) MinecraftMMO.getInstance().getClassHandler()
					.getClass(clazz.get("Class").getAsString()).clone();
			mc.setMoney(mon);
			mc.setLevel(level);
			mc.setClassLocation(loc);
			mc.setXp(xp);
			mc.setDamage(damage);
			mc.setArmor(defense);

			// Get their crafting skill
			JsonObject craftSkill = clazz.get("CraftingSkill").getAsJsonObject();
			mc.getCraftSkill().setLevel(craftSkill.get("Level").getAsInt());
			mc.getCraftSkill().setXp(craftSkill.get("XP").getAsDouble());

			// Get all MMORecipes and add it to that MMOClasses list
			JsonArray craftingRecipes = clazz.get("CraftingRecipes").getAsJsonArray();
			craftingRecipes.forEach(e -> {
				String recipe = e.toString();
				if (MinecraftMMO.getInstance().getRecipeManager().getRecipeByName(recipe) == null) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, recipe + " Does not exist in player's "
							+ p.getName() + " save file with UUID: " + p.getUniqueId().toString());
					return;
				}
				if (mc.getCraftingRecipes().contains(recipe))
					return;
				mc.getCraftingRecipes().add(recipe);
			});

			// Load all Abilities that MMOClass has into their playerAbilities list
			JsonArray playerAbilities = clazz.get("PlayerAbilities").getAsJsonArray();
			playerAbilities.forEach(e -> {
				String ability = e.toString();
				if (!MinecraftMMO.getInstance().getAbilityManager().getAbilities().containsKey(ability)) {
					MinecraftMMO.getInstance().getLogger().log(Level.WARNING, ability + " Does not exist in player's "
							+ p.getName() + " save file with UUID: " + p.getUniqueId().toString());
					return;
				}
				if (mc.getPlayerAbilities().contains(ability))
					return;
				mc.getPlayerAbilities().add(ability);
			});

			// Load all active quests
			JsonArray active = clazz.get("Active").getAsJsonArray();
			active.forEach(e -> {
				JsonObject inProg = e.getAsJsonObject();
				if (mc.getActiveQuests()
						.contains(MinecraftMMO.getInstance().getQuestManager().getQuest(inProg.get("Name").getAsString())))
					return;
				Object q = MinecraftMMO.getInstance().getQuestManager().getQuest(inProg.get("Name").getAsString()).clone();

				if (inProg.has("Properties")) {
					JsonObject questProperties = inProg.get("Properties").getAsJsonObject();
					((Quest) q).loadQuestToPlayer(questProperties);
				}

				mc.getActiveQuests().add((Quest) q);
			});

			// Load all completable quests
			JsonArray completable = clazz.get("Completable").getAsJsonArray();
			completable.forEach(e -> {
				if (mc.getCompletedableQuests().contains(e.toString()))
					return;
				mc.getCompletedableQuests().add(e.toString());
			});

			// Load all completed quests
			JsonArray completed = clazz.get("Completed").getAsJsonArray();
			completed.forEach(e -> {
				if (mc.getCompletedQuests().contains(e.toString()))
					return;
				mc.getCompletedQuests().add(e.toString());
			});

			// Load the MMOClasses inventory back to the state they saved it as
			JsonObject inv = clazz.get("Inventory").getAsJsonObject();

			mc.setHelmet(ItemUtils.JSONToItem(inv.get("Helmet").getAsJsonObject()).getName());
			mc.setChest(ItemUtils.JSONToItem(inv.get("Chestplate").getAsJsonObject()).getName());
			mc.setLegs(ItemUtils.JSONToItem(inv.get("Leggings").getAsJsonObject()).getName());
			mc.setBoots(ItemUtils.JSONToItem(inv.get("Boots").getAsJsonObject()).getName());
			mc.setMainH(ItemUtils.JSONToItem(inv.get("MainHand").getAsJsonObject()).getName());
			mc.setOffH(ItemUtils.JSONToItem(inv.get("OffHand").getAsJsonObject()).getName());

			mc.setMaxHealth(clazz.get("MaxHealth").getAsDouble());
			mc.setHealth(clazz.get("Health").getAsDouble());
			mc.setMana(clazz.get("Mana").getAsDouble());
			
			mp.getMmoClasses().add(mc);
		}

		// Create an NPC that has the same armor and weapons as the MMOClass and look at
		// the player when they load in to select them for their MMOClass
		int i = 0;
		List<NPC> playerOnlyNPCs = new ArrayList<>();
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		for (MMOClass mc : mp.getMmoClasses()) {
			NPC npc = new NPC(MinecraftMMO.getInstance().getMmoConfiguration().getClassSpawnLocations().get(i),
					"Character " + String.valueOf(i));
			npc.spawnNPC(p);
			npc.setHeldClass(mc);
			npc.setEquipment(p, ItemSlot.HELMET, cim.getItem(mc.getHelmet()).makeItem());
			npc.setEquipment(p, ItemSlot.CHESTPLATE, cim.getItem(mc.getChest()).makeItem());
			npc.setEquipment(p, ItemSlot.LEGGINGS, cim.getItem(mc.getLegs()).makeItem());
			npc.setEquipment(p, ItemSlot.BOOTS, cim.getItem(mc.getBoots()).makeItem());
			npc.setEquipment(p, ItemSlot.MAIN_HAND, cim.getItem(mc.getMainH()).makeItem());
			npc.setEquipment(p, ItemSlot.OFF_HAND, cim.getItem(mc.getOffH()).makeItem());
			playerOnlyNPCs.add(npc);
			npc.lookAtPlayer(p, p);
			i++;
		}

		playerNPCs.put(mp, playerOnlyNPCs);
		addPlayer(mp);
	}

	/**
	 * Checks if the Entity ID is a player's class NPC
	 * 
	 * @param p  Player to check
	 * @param id Entity ID to check
	 * @return if the NPC is players NPCs
	 */
	public boolean isPlayerClassNPC(Player p, int id) {
		MMOPlayer mp = getPlayer(p);
		for (NPC npc : playerNPCs.get(mp)) {
			if (Objects.equal(npc.getEntityID(), id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	public NPC getPlayerClassNPC(Player p, int id) {
		MMOPlayer mp = getPlayer(p);
		for (NPC npc : playerNPCs.get(mp)) {
			if (Objects.equal(npc.getEntityID(), id)) {
				return npc;
			}
		}
		return null;
	}

	/**
	 * Saves the players current MMOPlayer and all information in their MMOClasses
	 * 
	 * @param p Player to save
	 */
	public void savePlayerInfo(Player p) {
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);

		// Delete the players file and create a new one
		if (mp.getCurrentCharacter() != -1) {
			File f = new File(playersFolder, p.getUniqueId() + ".json");
			if (f.exists())
				f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JsonObject jo = new JsonObject();
			JsonObject classes = new JsonObject();
			int i = 1;

			// TODO: Fix this on a per class basis. Since right now all classes with have
			// the exact same item all the time
			for (MMOClass mc : mp.getMmoClasses()) {
				if (mc.equals(mp.getMmoClasses().get(mp.getCurrentCharacter()))) {
					classes.add("Class" + String.valueOf(i), saveClassInformation(p, mc));
				}
				i++;
			}

			// Save the classes JSON object and then format it in a nice 4 tabbed space JSON
			// string
			jo.add("Classes", classes);
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = g.toJson(jo);
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(prettyJson);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		removePlayer(p);
	}

	/**
	 * Create a new player file with the {} for JSON interpretation
	 * 
	 * @param p Player to create file
	 */
	public void createPlayerFile(Player p) {
		File f = new File(playersFolder, p.getUniqueId() + ".json");
		if (f.exists())
			return;
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintWriter pw = new PrintWriter(f, "UTF-8");
			pw.print("{");
			pw.print("}");
			pw.flush();
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Get MMOPlayer from Player
	 * 
	 * @param p Player to get MMOPlayer from
	 * @return MMOPlayer or Null
	 */
	public MMOPlayer getPlayer(Player p) {
		return players.stream().filter(e -> e.getPlayerUUID().equals(p.getUniqueId())).findFirst().orElse(null);
	}

	private JsonObject saveClassInformation(Player p, MMOClass mc) {
		JsonObject clazz = new JsonObject();
		String s = mc.getName();
		String loc = LocationUtils.locationSerializer(mc.getClassLocation());
		int level = mc.getLevel();
		double mon = mc.getMoney();
		double xp = mc.getXp();
		double damage = mc.getDamage();
		double defense = mc.getArmor();

		clazz.addProperty("Class", s);
		clazz.addProperty("Level", level);
		clazz.addProperty("XP", xp);
		clazz.addProperty("Currency", mon);
		clazz.addProperty("Location", loc);
		clazz.addProperty("Damage", damage);
		clazz.addProperty("Defense", defense);

		// Save the players Crafting Skill
		CraftingSkill ck = mc.getCraftSkill();
		JsonObject craftSkill = new JsonObject();
		craftSkill.addProperty("Level", ck.getLevel());
		craftSkill.addProperty("XP", ck.getXp());

		// Save all MMORecipes the MMOClass has
		JsonArray craftingRecipes = StringHelper.stringListToArray(mc.getCraftingRecipes());
		// Save all Abilities the MMOClass has
		JsonArray playerAbilities = StringHelper.stringListToArray(mc.getPlayerAbilities());
		// Save all Completed Quests the MMOClass has
		JsonArray completed = StringHelper.stringListToArray(mc.getCompletedQuests());
		// Save all Completable Quests the MMOClass has
		JsonArray completable = StringHelper.stringListToArray(mc.getCompletedableQuests());

		// Save all Active Quests the MMOClass has
		JsonArray inProg = new JsonArray();
		for (Quest q : mc.getActiveQuests()) {
			if(StringHelper.hasValue(inProg, q.getName())) continue;
			// Save the current progress on that Quest into the Quests properties
			JsonObject questJO = new JsonObject();
			questJO.addProperty("Name", q.getName());
			JsonObject questProperties = q.saveQuest();
			if (questProperties != null)
				questJO.add("Properties", q.saveQuest());
			inProg.add(questJO);
		}

		clazz.add("CraftingSkill", craftSkill);
		clazz.add("CraftingRecipes", craftingRecipes);
		clazz.add("PlayerAbilities", playerAbilities);
		clazz.add("Completed", completed);
		clazz.add("Completable", completable);
		clazz.add("Active", inProg);

		// Save each slot the player has, if it is null, set it as the default item
		// MMO_ITEM_EMPTY_SLOT_ITEM
		JsonObject inv = new JsonObject();
		CustomItemManager cim = MinecraftMMO.getInstance().getItemManager();
		inv.add("Helmet", (p.getInventory().getHelmet() != null) ? ItemUtils.itemToJSON(cim.getItem(mc.getHelmet()))
				: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
		inv.add("Chestplate",
				(p.getInventory().getChestplate() != null) ? ItemUtils.itemToJSON(cim.getItem(mc.getChest()))
						: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
		inv.add("Leggings", (p.getInventory().getLeggings() != null) ? ItemUtils.itemToJSON(cim.getItem(mc.getLegs()))
				: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
		inv.add("Boots", (p.getInventory().getBoots() != null) ? ItemUtils.itemToJSON(cim.getItem(mc.getBoots()))
				: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
		inv.add("MainHand", (p.getInventory().getItem(0) != null) ? ItemUtils.itemToJSON(cim.getItem(mc.getMainH()))
				: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
		inv.add("OffHand", (p.getInventory().getItem(1) != null) ? ItemUtils.itemToJSON(cim.getItem(mc.getOffH()))
				: ItemUtils.itemToJSON(cim.getItem("MMO_ITEM_EMPTY_SLOT_ITEM")));
		clazz.add("Inventory", inv);

		clazz.addProperty("MaxHealth", mc.getMaxHealth());
		clazz.addProperty("Health", p.getHealth());
		clazz.addProperty("Mana", mc.getMana());
		clazz.addProperty("Defense", mc.getArmor());
		clazz.addProperty("Damage", mc.getDamage());
		
		return clazz;
	}

}