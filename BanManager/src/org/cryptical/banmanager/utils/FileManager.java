package org.cryptical.banmanager.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.utils.enums.FileType;

public class FileManager {
	
	public static void saveYamls(FileType file, boolean announce) {
		switch(file) {
		case CONFIG: save(Core.configFile, Core.config, announce); break;
		case USERDATA: save(Core.userdataFile, Core.userdata, announce); break;
		case ALL:
			save(Core.configFile, Core.config, announce);
			save(Core.userdataFile, Core.userdata, announce);
			break;
		}
	}
	
	public static void loadYamls(FileType file, boolean announce) {
		switch(file) {
		case CONFIG: load(Core.configFile, Core.config, announce); break;
		case USERDATA: load(Core.userdataFile, Core.userdata, announce); break;
		case ALL:
			load(Core.configFile, Core.config, announce);
			load(Core.userdataFile, Core.userdata, announce);
			break;
		}
	}
	
	private static void save(File file, YamlConfiguration conf, boolean announce) {
		try {
			conf.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (announce) Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_AQUA + "Saved " + file.getName());
	}
	
	private static void load(File file, YamlConfiguration conf, boolean announce) {
		try {
			conf.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (announce) Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_AQUA + "Loaded " + file.getName());
	}
	
	
}
