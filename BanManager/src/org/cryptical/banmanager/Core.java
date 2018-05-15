package org.cryptical.banmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.cryptical.banmanager.commands.COMMAND_Ban;
import org.cryptical.banmanager.commands.COMMAND_Kick;
import org.cryptical.banmanager.commands.COMMAND_Mute;
import org.cryptical.banmanager.commands.COMMAND_Punish;
import org.cryptical.banmanager.commands.COMMAND_Tempban;
import org.cryptical.banmanager.commands.COMMAND_Tempmute;
import org.cryptical.banmanager.commands.COMMAND_Unban;
import org.cryptical.banmanager.commands.COMMAND_Unmute;
import org.cryptical.banmanager.commands.MainCommand;
import org.cryptical.banmanager.events.onInventoryClickEvent;
import org.cryptical.banmanager.events.onPlayerChatEvent;
import org.cryptical.banmanager.events.onPlayerJoinEvent;
import org.cryptical.banmanager.events.onPlayerPunishedEvent;
import org.cryptical.banmanager.events.onPlayerQuitEvent;
import org.cryptical.banmanager.gui.GuiManager;
import org.cryptical.banmanager.lang.LangSetup;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.player.PlayerData;
import org.cryptical.banmanager.utils.Database;
import org.cryptical.banmanager.utils.FileManager;
import org.cryptical.banmanager.utils.Metrics;
import org.cryptical.banmanager.utils.enums.FileType;
import org.cryptical.guiapi.GuiAPI;
import org.cryptical.guiapi.MainGui;

public class Core extends JavaPlugin{
	
	public Core main;
	
	public Core getInstance() {
		return main;
	}

	public static Plugin pl;
	
    public static File configFile;
    public static File userdataFile;
    public static YamlConfiguration config;
	public static YamlConfiguration userdata;

	private File langENFile;
	
	public static PlayerData playerData;
	public static MainGui maingui;
	public static GuiAPI guiApi;
	public static GuiManager guiManager;
	
	public static LangSetup languages;

    public void onEnable(){
		this.main = this;
        startup();
        
        new Metrics(this);
    }

    public void onDisable(){
    	playerData.save();
    	FileManager.saveYamls(FileType.USERDATA, false);
    }

    public void startup() {
		pl = this;
    	setupFiles();
    	FileManager.loadYamls(FileType.ALL, true);
    	registerEvents();
    	registerPermissions();
    	registerCommands();
    	
    	if (config.getBoolean("database.enabled")) {
    		try {
    			Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.GREEN + "Using MySQL as data storage");
    			Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.GREEN + "Connecting to the MySQL database...");
    			Database.connect();
    		} catch (SQLException e) {
    			e.printStackTrace();
    			Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_RED + "Cannot connect to database, disabling plugin");
    			Bukkit.getPluginManager().disablePlugin(this);
    			return;
    		}
    		Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.GREEN + "Succesfully connected to the database");
    		Database.createFirstTables();
    	}
    	
    	if (!config.getString("config_version").equals(getDescription().getVersion())) {
			Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_RED + "Config is outdated, disabling plugin");
			Bukkit.getPluginManager().disablePlugin(this);
    	}

    	languages = new LangSetup();
    	playerData = new PlayerData();
    	maingui = new MainGui();
    	guiApi = maingui.get();
    	guiManager = new GuiManager();
    	run();
    }
    
    public void setupFiles() {
		configFile = new File(getDataFolder(), "config.yml");
		userdataFile = new File(getDataFolder(), "userdata.yml");
		langENFile = new File(getDataFolder() + File.separator + "languages", "lang_en.yml");
		firstRun();
		config = new YamlConfiguration();
		userdata = new YamlConfiguration();
	}
    
    public void firstRun() {
		try {
			if (!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				copy(getResource("config.yml"), configFile);
			}
			if (!userdataFile.exists()) {
				userdataFile.getParentFile().mkdirs();
				copy(getResource("userdata.yml"), userdataFile);
			}
			if (!langENFile.exists()) {
				langENFile.getParentFile().mkdirs();
				copy(getResource("lang_en.yml"), langENFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while((len=in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new onPlayerJoinEvent(), this);
        pm.registerEvents(new onPlayerQuitEvent(), this);
        pm.registerEvents(new onPlayerChatEvent(), this);
        pm.registerEvents(new onInventoryClickEvent(), this);
        pm.registerEvents(new onPlayerPunishedEvent(), this);
    }
    
    public void registerCommands() {
    	getCommand("banmanager").setExecutor(new MainCommand());
    	getCommand("ban").setExecutor(new COMMAND_Ban());
    	getCommand("tempban").setExecutor(new COMMAND_Tempban());
    	getCommand("unban").setExecutor(new COMMAND_Unban());
    	getCommand("kick").setExecutor(new COMMAND_Kick());
    	getCommand("mute").setExecutor(new COMMAND_Mute());
    	getCommand("tempmute").setExecutor(new COMMAND_Tempmute());
    	getCommand("unmute").setExecutor(new COMMAND_Unmute());
    	getCommand("punish").setExecutor(new COMMAND_Punish());
    	
    }

    public void registerPermissions() {
    	List<String> permissions = Arrays.asList(
    			"banmanager.command.*",
    			"banmanager.command.use",
    			"banmanager.command.userinfo",
    			"banmanager.command.reload",
    			"banmanager.command.ban",
    			"banmanager.command.tempban",
    			"banmanager.command.unban",
    			"banmanager.command.mute",
    			"banmanager.command.tempmute",
    			"banmanager.command.unmute",
    			"banmanager.command.kick",
    			"banmanager.command.punish",
    			"banmanager.command.banlist",
    			"banmanager.command.mutelist",
    			"banmanager.command.reset",
    			"banmanager.command.gui",
    			"banmanager.command.language"
    		);

    	PluginManager pm = getServer().getPluginManager();
    	for (String s : permissions) {
    		Permission perm = new Permission(s);
    		pm.addPermission(perm);
    	}
    }
    
    public void run() {
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
    		public void run() {
    			playerData.load();
    		}
    	}, 20, config.getInt("settings.sync-interval")*20);
    	
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
    		public void run() {
    			for (CPlayer player : playerData.getPlayers()) {
    				player.update();
    			}
    		}
    	}, 20, 60*20);
    }
}
