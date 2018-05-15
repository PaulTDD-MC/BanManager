package org.cryptical.banmanager.lang.languages;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.lang.messages.Message;
import org.cryptical.banmanager.utils.Methods;

public class Language {

	private File file;
	private YamlConfiguration conf;
	private String tag;
	private String name;
	
	public Language(String name, String tag) {
		this.tag = tag;
		this.name = name;
		
		this.file = new File(Core.pl.getDataFolder() + File.separator + "languages" + File.separator + "lang_"+this.tag+".yml");
		this.conf = new YamlConfiguration();
		
		if (this.file == null) {
			Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_RED + "Could not load language " + name + "!");
			return;
		}
		
		try { this.conf.load(this.file);
		} catch (Exception e) {
			e.printStackTrace(); }
		Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.GREEN + "Loaded language " + name + " [" + tag + "] succesfully");
	}
	
	public String getLanguageTag() {
		return this.tag;
	}
	
	public String getLanguageName() {
		return this.name;
	}

	public String getMessage(Message message) {
		String msg = this.conf.getString(message.getPath());
		if (msg.contains("%target%")) msg = msg.replace("%target%", message.getTarget());
		if (msg.contains("%language%")) msg = msg.replace("%language%", message.getLanguage());
		return Methods.inColors(msg);
	}

	public File getFile() {
		return this.file;
	}

	public YamlConfiguration getConf() {
		return this.conf;
	}
	
}
