package org.cryptical.banmanager.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.lang.messages.NotAllowedToChat;
import org.cryptical.banmanager.player.CPlayer;

public class onPlayerChatEvent implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		CPlayer player = Core.playerData.getPlayer(e.getPlayer().getUniqueId());
		if (player.isMuted()) {
			e.setCancelled(true);
			player.sendMessage(player.getLanguage().getMessage(new NotAllowedToChat()));
		}
	}
}
