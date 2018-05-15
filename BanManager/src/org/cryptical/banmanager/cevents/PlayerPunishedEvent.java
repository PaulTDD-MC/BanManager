package org.cryptical.banmanager.cevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.Punisher.PunishReason;

public class PlayerPunishedEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private CPlayer player;
	private PunishReason punish;
	
	public PlayerPunishedEvent(CPlayer player, PunishReason punish) {
		this.player = player;
		this.punish = punish;
	}
	
	public PunishReason getPunishReason() {
		return this.punish;
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
