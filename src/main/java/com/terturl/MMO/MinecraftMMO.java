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
import com.terturl.MMO.Util.NPCManager;
import com.terturl.MMO.Util.Items.CustomItemManager;
import com.terturl.MMO.Util.Listeners.DamageEvent;
import com.terturl.MMO.Util.Listeners.EntityDeathListeners;
import com.terturl.MMO.Util.Listeners.HotbarListeners;
import com.terturl.MMO.Util.Listeners.InteractNPCListener;
import com.terturl.MMO.Util.Listeners.ItemInteractionListeners;
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
	
	// CustomMobs {Level} | {Name} | {Health}
	// Test 2
	public void onEnable() {
		instance = this;
		if(!getDataFolder().exists()) getDataFolder().mkdir();
		
		mathConfig = new MathConfiguration();
		
		try {
			itemManager = new CustomItemManager();
			recipeManager = new RecipeManager();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		classHandler = new ClassHandler();
		registerClasses();
		questManager = new QuestManager();
		npcHandler = new NPCManager();
		playerHandler = new PlayerHandler();
		mmoConfiguration = new Configuration();
		abilityManager = new AbilityManager();
		
		shopManager = new ShopManager();
		entityManager = new MMOEntityManager();
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
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new InteractNPCListener(), this);
		pm.registerEvents(new ItemInteractionListeners(), this);
		pm.registerEvents(new PlayerMoveListeners(), this);
		pm.registerEvents(new HotbarListeners(), this);
		pm.registerEvents(new DamageEvent(), this);
		pm.registerEvents(new EntityDeathListeners(), this);
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