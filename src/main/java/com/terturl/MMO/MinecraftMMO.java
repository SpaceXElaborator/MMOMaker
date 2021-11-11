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
import com.terturl.MMO.Effects.MiscEffects.LineEffect;
import com.terturl.MMO.Effects.MiscEffects.WaveEffect;
import com.terturl.MMO.Effects.PlayerEffects.HealEffect;
import com.terturl.MMO.Effects.PlayerEffects.ProjectileEffect;
import com.terturl.MMO.Effects.TimeBasedEffects.LimitEffect;
import com.terturl.MMO.Effects.TimeBasedEffects.RepeatingEffect;
import com.terturl.MMO.Effects.VectorEffects.JumpTo;
import com.terturl.MMO.Effects.VectorEffects.PullTo;
import com.terturl.MMO.Effects.VectorEffects.VectorDirection;
import com.terturl.MMO.Effects.VectorEffects.VectorPush;
import com.terturl.MMO.Effects.VectorEffects.VectorRelative;
import com.terturl.MMO.Entity.MMOEntityManager;
import com.terturl.MMO.Entity.NPC.NPCManager;
import com.terturl.MMO.Files.Configuration;
import com.terturl.MMO.Files.MathConfiguration;
import com.terturl.MMO.Framework.CommandMeta;
import com.terturl.MMO.Framework.CraftCommand;
import com.terturl.MMO.MMOEntity.MMOMobManager;
import com.terturl.MMO.MMOEntity.MobFileReader;
import com.terturl.MMO.Player.PlayerHandler;
import com.terturl.MMO.Player.MMOClasses.ClassHandler;
import com.terturl.MMO.Player.Shops.ShopManager;
import com.terturl.MMO.Player.Skills.Crafting.RecipeManager;
import com.terturl.MMO.Player.Skills.Herbalism.HerbalismInteract;
import com.terturl.MMO.Player.Skills.Herbalism.HerbalismManager;
import com.terturl.MMO.Quests.QuestManager;
import com.terturl.MMO.Quests.Subquests.CustomCraftQuest;
import com.terturl.MMO.Quests.Subquests.EntityKillQuest;
import com.terturl.MMO.Quests.Subquests.LocationQuest;
import com.terturl.MMO.Quests.Subquests.MMOEntityKillQuest;
import com.terturl.MMO.Quests.Subquests.MMOItemCollectQuest;
import com.terturl.MMO.Quests.Subquests.NPCTalkQuest;
import com.terturl.MMO.Util.Items.CustomItemManager;
import com.terturl.MMO.Util.Listeners.DamageEvent;
import com.terturl.MMO.Util.Listeners.EntityDeathListeners;
import com.terturl.MMO.Util.Listeners.HotbarListeners;
import com.terturl.MMO.Util.Listeners.InteractNPCListener;
import com.terturl.MMO.Util.Listeners.ItemInteractionListeners;
import com.terturl.MMO.Util.Listeners.MMOEntityDeathListener;
import com.terturl.MMO.Util.Listeners.MMOItemPickUpListener;
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
	private ShopManager shopManager;
	@Getter
	private RecipeManager recipeManager;
	@Getter
	private MMOEntityManager entityManager;

	@Getter
	private MobFileReader mobReader;
	@Getter
	private MMOMobManager mobManager;

	// Skills
	@Getter
	private HerbalismManager herbalismManager;

	private static MinecraftMMO instance;

	public void onLoad() {
		instance = this;
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		questManager = new QuestManager();
		abilityManager = new AbilityManager();
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

		registerEffects();
		abilityManager.addAbilities();
		classHandler = new ClassHandler();
		registerQuestTypes();
		questManager.loadQuests();
		npcHandler = new NPCManager();
		playerHandler = new PlayerHandler();
		mmoConfiguration = new Configuration();
		
		mobManager = new MMOMobManager();
		mobReader = new MobFileReader();

		shopManager = new ShopManager();

		// Skills
		herbalismManager = new HerbalismManager();

		try {
			entityManager = new MMOEntityManager();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.getServer().getConsoleSender().sendMessage("Enabling MMO Plugin");
		registerCommand(new TestCommand());

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
		registerListener(new MMOItemPickUpListener());

		registerListener(new HerbalismInteract());
	}

	private void registerQuestTypes() {
		questManager.registerQuest("Location", new LocationQuest());
		questManager.registerQuest("KillEntity", new EntityKillQuest());
		questManager.registerQuest("TalkTo", new NPCTalkQuest());
		questManager.registerQuest("CraftItem", new CustomCraftQuest());
		questManager.registerQuest("CollectItem", new MMOItemCollectQuest());
		questManager.registerQuest("MMOKillEntity", new MMOEntityKillQuest());
	}
	
	private void registerEffects() {
		abilityManager.registerEffect("Line", new LineEffect());
		abilityManager.registerEffect("Wave", new WaveEffect());
		abilityManager.registerEffect("Heal", new HealEffect());
		abilityManager.registerEffect("Projectile", new ProjectileEffect());
		abilityManager.registerEffect("Limit", new LimitEffect());
		abilityManager.registerEffect("Repeating", new RepeatingEffect());
		abilityManager.registerEffect("JumpTo", new JumpTo());
		abilityManager.registerEffect("PullTo", new PullTo());
		abilityManager.registerEffect("Direction", new VectorDirection());
		abilityManager.registerEffect("Push", new VectorPush());
		abilityManager.registerEffect("VectorRelative", new VectorRelative());
	}

	/**
	 * Method used to programmically add commands to the plugin.yml instead of
	 * having to do it manually. Mainly since I always forget to do it, this allows
	 * me to be lazy and add the command automatically
	 * 
	 * @param <T> Command to register
	 * @param command
	 */
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