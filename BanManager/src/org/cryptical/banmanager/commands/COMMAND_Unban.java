package org.cryptical.banmanager.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.cevents.PlayerPunishedEvent;
import org.cryptical.banmanager.cevents.PlayerUnbanEvent;
import org.cryptical.banmanager.lang.languages.Language;
import org.cryptical.banmanager.lang.messages.InsufficientArguments;
import org.cryptical.banmanager.lang.messages.NoPermission;
import org.cryptical.banmanager.lang.messages.PlayerNotFound;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.Punisher;
import org.cryptical.banmanager.utils.Punisher.PunishReason;

public class COMMAND_Unban implements CommandExecutor, TabCompleter{
	
	// /unban <player>
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Language lang = Core.languages.getLang(Core.config.getString("settings.default_language"));
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			CPlayer cp = Core.playerData.getPlayer(player.getUniqueId());
			lang = cp.getLanguage();
			if (!player.hasPermission("banmanager.command.ban") && !player.hasPermission("banmanager.command.*")) {
				player.sendMessage(lang.getMessage(new NoPermission()));
				return true;
			}
		}
		
		if (!(args.length >= 1)) {
			sender.sendMessage(lang.getMessage(new InsufficientArguments()));
			return true;
		}
		
		CPlayer target = Core.playerData.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(lang.getMessage(new PlayerNotFound(args[0])));
			return true;
		}

		target.setBannedBy( (sender instanceof Player) ? ((Player)sender).getName() : "console" );
		Punisher.unban(target);

		Bukkit.getPluginManager().callEvent(new PlayerUnbanEvent(target));
		Bukkit.getPluginManager().callEvent(new PlayerPunishedEvent(target, PunishReason.UNBAN));
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> matches = new ArrayList<>();
		for (CPlayer p : Core.playerData.getPlayers()) {
			if (p.isBanned()) {
				if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
					matches.add(p.getName());
				}
			}
		}
		return matches;
	}
}

