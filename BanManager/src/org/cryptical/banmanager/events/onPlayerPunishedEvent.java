package org.cryptical.banmanager.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.cevents.PlayerPunishedEvent;

public class onPlayerPunishedEvent implements Listener {
	
	@EventHandler
	public void onPlayerPunished(PlayerPunishedEvent e) {
		Core.guiManager.clearAll();
		Core.guiManager.fill();
		Core.guiManager.setupPages();
		
		e.getPlayer().log(e.getPunishReason());
	}
}


