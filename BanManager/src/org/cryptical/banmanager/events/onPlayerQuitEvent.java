package org.cryptical.banmanager.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.player.CPlayer;

public class onPlayerQuitEvent implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		CPlayer player = Core.playerData.newPlayer(e.getPlayer().getUniqueId());
		if (player.isBanned()) {
			e.setQuitMessage(null);
		}
	}
}
