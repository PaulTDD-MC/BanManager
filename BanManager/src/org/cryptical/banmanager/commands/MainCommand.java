package org.cryptical.banmanager.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.gui.GuiManager.GuiType;
import org.cryptical.banmanager.lang.languages.Language;
import org.cryptical.banmanager.lang.messages.BanlistEmpty;
import org.cryptical.banmanager.lang.messages.CommandNotFound;
import org.cryptical.banmanager.lang.messages.InsufficientArguments;
import org.cryptical.banmanager.lang.messages.LanguageChanged;
import org.cryptical.banmanager.lang.messages.LanguageInvalid;
import org.cryptical.banmanager.lang.messages.LanguageSet;
import org.cryptical.banmanager.lang.messages.MutelistEmpty;
import org.cryptical.banmanager.lang.messages.NoPermission;
import org.cryptical.banmanager.lang.messages.PlayerNotFound;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.Database;
import org.cryptical.banmanager.utils.FileManager;
import org.cryptical.banmanager.utils.Methods;
import org.cryptical.banmanager.utils.enums.FileType;
import org.cryptical.banmanager.utils.serializers.DateUtils;

public class MainCommand implements CommandExecutor, TabCompleter{

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Language lang = Core.languages.getLang(Core.config.getString("settings.default_language"));
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			CPlayer cp = Core.playerData.getPlayer(player.getUniqueId());
			lang = cp.getLanguage();
			if (!player.hasPermission("banmanager.command.use") && !player.hasPermission("banmanager.command.*")) {
				player.sendMessage(lang.getMessage(new NoPermission()));
				return true;
			}
		}
		
		// /banmanager help <page>
		if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help") || args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("1")) {
			for (String string : lang.getConf().getStringList("help.1")) {
				sender.sendMessage(Methods.inColors(string));
			}
		}
		
		else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equals("2")) {
			for (String string : lang.getConf().getStringList("help.2")) {
				sender.sendMessage(Methods.inColors(string));
			}
		}
		
		else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equals("3")) {
			for (String string : lang.getConf().getStringList("help.3")) {
				sender.sendMessage(Methods.inColors(string));
			}
		}
		
		// /banmanager language <lang> [player]
		else if (Methods.equalsCommand(args[0], Arrays.asList("language","setlanguage","setlang","lang"))) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("banmanager.command.language") && !player.hasPermission("banmanager.command.*")) {
					player.sendMessage(lang.getMessage(new NoPermission()));
					return true;
				}
			}
			
			CPlayer target = null;
			if (sender instanceof Player) {
				target = Core.playerData.getPlayer(((Player)sender).getUniqueId());
			}
			if (args.length == 3) {
				target = Core.playerData.getPlayer(args[2].toLowerCase());
				if (target == null) {
					sender.sendMessage(lang.getMessage(new PlayerNotFound(args[2])));
					return true;
				}
			}
			if (args.length == 1) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(lang.getMessage(new InsufficientArguments()));
					return true;
				}
				((Player)sender).openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(GuiType.lang), 1));
				return true;
			}
			
			if (target == null && args.length == 2) {
				sender.sendMessage(lang.getMessage(new InsufficientArguments()));
				return true;
			}
			
			String newlang = args[1];
			Language setlang = null;
			for (String s : Core.languages.get().keySet()) {
				if (newlang.toLowerCase().equalsIgnoreCase(s.toLowerCase())) {
					setlang = Core.languages.get().get(s);
				}
			}
			if (setlang == null) {
				sender.sendMessage(lang.getMessage(new LanguageInvalid()));
				return true;
			}
			sender.sendMessage(lang.getMessage(new LanguageSet(target.getName(), setlang.getLanguageName())));
			target.setLanguage(setlang);
			lang = setlang;
			target.sendMessage(lang.getMessage(new LanguageChanged(setlang.getLanguageName())));
			if (sender instanceof Player) {
				Player p = (Player)sender;
				if (p.getName().equals(target.getName())) return true;
			}
		}
		
		// banmanager userinfo <sender>
		else if (Methods.equalsCommand(args[0], Arrays.asList("userinfo"))) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("banmanager.command.userinfo") && !player.hasPermission("banmanager.command.*")) {
					player.sendMessage(lang.getMessage(new NoPermission()));
					return true;
				}
			}
			
			if (args.length != 2) {
				sender.sendMessage(lang.getMessage(new InsufficientArguments()));
				return true;
			}
			
			CPlayer target = Core.playerData.getPlayer(args[1]);
			if (target == null) {
				sender.sendMessage(lang.getMessage(new PlayerNotFound(args[1])));
				return true;
			}
			
			sender.sendMessage(" ");
			sender.sendMessage("§8§m--------§8[§b"+target.getName()+" info§8]§8§m--------");
			sender.sendMessage(" §7Banned: §9" + target.isBanned());
			sender.sendMessage(" §7Permbanned: §9" + target.isPermBanned());
			sender.sendMessage(" §7Muted: §9" + target.isMuted());
			sender.sendMessage(" §7Permmuted: §9" + target.isPermMuted());
			sender.sendMessage(" §7Times punished: §9" + target.timesBanned());
			sender.sendMessage(" §7Banned till: §9" + (target.isBanned() ? (target.isPermBanned() ? "permanent" : DateUtils.format(target.getBannedTill())) : "N/A"));
			sender.sendMessage(" §7Muted till: §9" + (target.isMuted() ? (target.isPermMuted() ? "permanent" : DateUtils.format(target.getMutedTill())) : "N/A"));
			sender.sendMessage(" §7Ban reason: §9" + (target.isBanned() ? target.getReason() : "none"));
			sender.sendMessage(" §7Active language: §9" + target.getLanguage().getLanguageName());
		}
		
		// /banmanager reload
		else if (Methods.equalsCommand(args[0], Arrays.asList("reload","rl"))) {
			String prefix = "[" + Core.pl.getDescription().getName() + "] ";
			
			if (sender instanceof Player) {
				prefix = "";
				Player player = (Player) sender;
				if (!player.hasPermission("banmanager.command.reload") && !player.hasPermission("banmanager.command.*")) {
					player.sendMessage(lang.getMessage(new NoPermission()));
					return true;
				}
			}
			
			sender.sendMessage(prefix + ChatColor.DARK_AQUA + "Reloading all data");
			FileManager.loadYamls(FileType.ALL, true);
			Core.playerData.load();
			Core.guiManager.setup();
			
			for (Language l : Core.languages.get().values()) {
				try { l.getConf().load(l.getFile());
				} catch (Exception e) { e.printStackTrace(); }
			}
			
			sender.sendMessage(prefix + ChatColor.DARK_AQUA + "Reload complete");
		}
		
		// /banmanager bans
		else if (Methods.equalsCommand(args[0], Arrays.asList("banlist","bans"))) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("banmanager.command.banlist") && !player.hasPermission("banmanager.command.*")) {
					player.sendMessage(lang.getMessage(new NoPermission()));
					return true;
				}
			}
			
			String list = "";
			for (CPlayer player : Core.playerData.getBannedCPlayers()) {
				list +=  ", " + player.getName();
			}
			list.replaceFirst(",", "");
			if (list.equals("")) {
				sender.sendMessage(lang.getMessage(new BanlistEmpty()));
				return true;
			}
			sender.sendMessage("§aHere is a list of all banned players:" + list);
		}
		
		// /banmanager mutes
		else if (Methods.equalsCommand(args[0], Arrays.asList("mutelist","mutes"))) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("banmanager.command.mutelist") && !player.hasPermission("banmanager.command.*")) {
					player.sendMessage(lang.getMessage(new NoPermission()));
					return true;
				}
			}
			
			String list = "";
			for (CPlayer player : Core.playerData.getMutedCPlayers()) {
				list +=  ", " + player.getName();
			}
			list.replaceFirst(",", "");
			if (list.equals("")) {
				sender.sendMessage(lang.getMessage(new MutelistEmpty()));
				return true;
			}
			sender.sendMessage("§aHere is a list of all muted players:" + list);
		}
		
		// /banmanager about
		else if (Methods.equalsCommand(args[0], Arrays.asList("about","info"))) {
			PluginDescriptionFile pdf = Core.pl.getDescription();
			sender.sendMessage("");
			sender.sendMessage("§8§m-----------§8[§b§l" + pdf.getName() + " about§8]§8§m-----------");
			sender.sendMessage(" §7Developers: §3CrypticalDevs [PaulTDD]");
			sender.sendMessage(" §7Version: §3" + pdf.getVersion());
			sender.sendMessage(" §7MySQL enabled: §3" + Database.isConnected());
		}
		
		// /banmanager reset <player>
		else if (Methods.equalsCommand(args[0], Arrays.asList("reset"))) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("banmanager.command.reset") && !player.hasPermission("banmanager.command.*")) {
					player.sendMessage(lang.getMessage(new NoPermission()));
					return true;
				}
			}
			
			CPlayer target = Core.playerData.getPlayer(args[1]);
			if (target == null) {
				sender.sendMessage(lang.getMessage(new PlayerNotFound(args[1])));
				return true;
			}
			
			target.setBanned(false);
			target.setMuted(false);
			target.resetBans();
		}
		
		// /banmanager gui
		else if (Methods.equalsCommand(args[0], Arrays.asList("gui"))) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
				return true;
			}
			
			Player player = (Player)sender;
			if (!player.hasPermission("banmanager.command.gui") && !player.hasPermission("banmanager.command.*")) {
				player.sendMessage(lang.getMessage(new NoPermission()));
				return true;
			}
			player.openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(GuiType.home), 1));
		}
		
		else {
			sender.sendMessage(lang.getMessage(new CommandNotFound()));
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> matches = new ArrayList<>();
		
		List<String> args0 = Arrays.asList("help","about","info","reload","rl","userinfo","banlist","bans","mutelist","mutes","reset","gui","language","setlanguage","setlang","lang");
		String search = args[0].toLowerCase();
		if (args.length == 1) {
			for (String list : args0) {
				if (list.startsWith(search)) {
					matches.add(list);
				}
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("help")) {
				for (String list : Arrays.asList("1","2","3")) {
					if (list.startsWith(args[1])) {
						matches.add(list);
					}
				}
			}
			if (args[0].equalsIgnoreCase("userinfo") || args[0].equalsIgnoreCase("reset")) {
				for (CPlayer player : Core.playerData.getPlayers()) {
					if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
						matches.add(player.getName());
					}
				}
			}
			if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("setlanguage") || args[0].equalsIgnoreCase("setlang") || args[0].equalsIgnoreCase("lang")) {
				for (String lang : Core.languages.get().keySet()) {
					if (lang.toLowerCase().startsWith(args[1].toLowerCase())) {
						matches.add(lang);
					}
				}
			}
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("setlanguage") || args[0].equalsIgnoreCase("setlang") || args[0].equalsIgnoreCase("lang")) {
				for (CPlayer player : Core.playerData.getPlayers()) {
					if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
						matches.add(player.getName());
					}
				}
			}
		}
		
		return matches;
	}
}
