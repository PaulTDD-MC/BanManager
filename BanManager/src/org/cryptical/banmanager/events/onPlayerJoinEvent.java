package org.cryptical.banmanager.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.Database;
import org.cryptical.banmanager.utils.Methods;
import org.cryptical.banmanager.utils.serializers.DateUtils;

public class onPlayerJoinEvent implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		CPlayer player = Core.playerData.newPlayer(e.getPlayer().getUniqueId());
		if (Core.config.getBoolean("database.enabled")) {
			if (!Database.contains(Database.table, e.getPlayer())) Database.put(e.getPlayer(), Database.table, "NAME", e.getPlayer().getName());
		}
		
		player.update();
		if (player.isBanned()) {
			e.setJoinMessage(null);
			String banmessage = "";
			
			for (String s : player.getLanguage().getConf().getStringList("ban-message")) {
				String line = s.replace("%reason%", player.getReason());
				line = line.replace("%till%", (player.isPermBanned() ? "never" : DateUtils.format(player.getBannedTill())) );
				line = line.replace("%bannedby%", player.getBannedBy());
				
				banmessage += line + "\n";
			}
			player.kick(Methods.inColors(banmessage));
		}
	}
}
