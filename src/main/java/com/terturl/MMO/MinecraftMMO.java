package com.terturl.MMO;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.terturl.MMO.Abilities.AbilityManager;
import com.terturl.MMO.Commands.TestCommand;
import com.terturl.MMO.CustomArmorStandMobs.ArmorStandMobController;
import com.terturl.MMO.Entity.MMOEntityManager;
import com.terturl.MMO.Entity.NPC.NPCManager;
import com.terturl.MMO.Files.Configuration;
import com.terturl.MMO.Files.MathConfiguration;
import com.terturl.MMO.Framework.CommandMeta;
import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.Player.PlayerHandler;
import com.terturl.MMO.Player.MMOClasses.ArcherClass;
import com.terturl.MMO.Player.MMOClasses.ClassHandler;
import com.terturl.MMO.Player.Shops.ShopManager;
import com.terturl.MMO.Player.Skills.Crafting.RecipeManager;
import com.terturl.MMO.Quests.QuestManager;
import com.terturl.MMO.Quests.Subquests.CustomCraftQuest;
import com.terturl.MMO.Quests.Subquests.EntityKillQuest;
import com.terturl.MMO.Quests.Subquests.LocationQuest;
import com.terturl.MMO.Quests.Subquests.NPCTalkQuest;
import com.terturl.MMO.Util.Items.CustomItemManager;
import com.terturl.MMO.Util.Listeners.DamageEvent;
import com.terturl.MMO.Util.Listeners.EntityDeathListeners;
import com.terturl.MMO.Util.Listeners.HotbarListeners;
import com.terturl.MMO.Util.Listeners.InteractNPCListener;
import com.terturl.MMO.Util.Listeners.ItemInteractionListeners;
import com.terturl.MMO.Util.Listeners.MMOEntityDeathListener;
import com.terturl.MMO.Util.Listeners.PlayerDropItemListener;
import com.terturl.MMO.Util.Listeners.PlayerJoinListener;
import com.terturl.MMO.Util.Listeners.PlayerMoveListeners;

import lombok.Getter;

public class MinecraftMMO extends JavaPlugin {

	@Getter
	private NPCManager npcHandler;
	@Getter
	private AbilityManager abilityManager;
	@Getter
	private PlayerHandler playerHandler;
	@Getter
	private Configuration mmoConfiguration;
	@Getter
	private MathConfiguration mathConfig;
	@Getter
	private ClassHandler classHandler;
	@Getter
	private CustomItemManager itemManager;
	@Getter
	private QuestManager questManager;
	@Getter
	private ArmorStandMobController mobController;
	@Getter
	private ShopManager shopManager;
	@Getter
	private RecipeManager recipeManager;
	@Getter
	private MMOEntityManager entityManager;
	
	private static MinecraftMMO instance;
	
	public void onLoad() {
		instance = this;
		if(!getDataFolder().exists()) getDataFolder().mkdir();
		questManager = new QuestManager();
	}
	
	// CustomMobs {Level} | {Name} | {Health}
	// Test 2
	public void onEnable() {
		mathConfig = new MathConfiguration();
		
		try {
			itemManager = new CustomItemManager();
			recipeManager = new RecipeManager();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		abilityManager = new AbilityManager();
		classHandler = new ClassHandler();
		registerClasses();
		registerQuestTypes();
		questManager.loadQuests();
		npcHandler = new NPCManager();
		playerHandler = new PlayerHandler();
		mmoConfiguration = new Configuration();
		
		shopManager = new ShopManager();
		
		try {
			entityManager = new MMOEntityManager();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.getServer().getConsoleSender().sendMessage("Enabling MMO Plugin");
		registerCommand(new TestCommand());
		
		mobController = new ArmorStandMobController();
		
		registerListeners();
	}
	
	public void onDisable() {
		entityManager.getAliveEntities().forEach(e -> {
			e.killEntity();
		});
		
		Bukkit.getOnlinePlayers().stream().forEach(e -> {
			playerHandler.savePlayerInfo(e);
			e.kickPlayer("Maintenence has been initiated, sorry for the inconvience");
		});
	}
	
	public static MinecraftMMO getInstance() {
		return instance;
	}
	
	private void registerClasses() {
		classHandler.registerClass(new ArcherClass());
	}
	
	public <T extends Listener> T registerListener(T listener) {
		Bukkit.getPluginManager().registerEvents((Listener) listener, (Plugin) this);
		return listener;
	}
	
	private void registerListeners() {
		registerListener(new PlayerJoinListener());
		registerListener(new InteractNPCListener());
		registerListener(new ItemInteractionListeners());
		registerListener(new PlayerMoveListeners());
		registerListener(new HotbarListeners());
		registerListener(new DamageEvent());
		registerListener(new EntityDeathListeners());
		registerListener(new MMOEntityDeathListener());
		registerListener(new PlayerDropItemListener());
	}
	
	private void registerQuestTypes() {
		questManager.registerQuest("Location", new LocationQuest());
		questManager.registerQuest("KillEntity", new EntityKillQuest());
		questManager.registerQuest("TalkTo", new NPCTalkQuest());
		questManager.registerQuest("CraftItem", new CustomCraftQuest());
	}
	
	public <T extends CraftCommand> void registerCommand(T command) {
		PluginCommand pg = getCommand(command.getName());
		if (pg == null) {
			try {
				Constructor<PluginCommand> cConstructor = PluginCommand.class
						.getDeclaredConstructor(new Class[] { String.class, Plugin.class });
				cConstructor.setAccessible(true);
				pg = cConstructor.newInstance(new Object[] { command.getName(), this });
			} catch (Exception e) {
				e.printStackTrace();
			}
			CommandMap cm = null;
			try {
				PluginManager pm = Bukkit.getPluginManager();
				Field cMapField = pm.getClass().getDeclaredField("commandMap");
				cMapField.setAccessible(true);
				cm = (CommandMap) cMapField.get(pm);
			} catch (Exception e) {
				e.printStackTrace();
			}
			CommandMeta anno = command.getClass().<CommandMeta>getAnnotation(CommandMeta.class);
			if (anno != null) {
				pg.setAliases(Arrays.asList(anno.aliases()));
				pg.setDescription(anno.description());
				pg.setUsage(anno.usage());
			}
			cm.register(getDescription().getName(), (Command) pg);
		}
		pg.setExecutor((CommandExecutor) command);
		pg.setTabCompleter((TabCompleter) command);
	}

}