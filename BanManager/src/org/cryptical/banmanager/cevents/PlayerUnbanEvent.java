package org.cryptical.banmanager.cevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cryptical.banmanager.player.CPlayer;

public class PlayerUnbanEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private CPlayer player;
	
	public PlayerUnbanEvent(CPlayer player) {
		this.player = player;
	}
	
	public CPlayer getPlayer() {
		return this.player;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
