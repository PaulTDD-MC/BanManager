package org.cryptical.banmanager.player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.cevents.PlayerPunishedEvent;
import org.cryptical.banmanager.cevents.PlayerUnbanEvent;
import org.cryptical.banmanager.cevents.PlayerUnmuteEvent;
import org.cryptical.banmanager.lang.languages.Language;
import org.cryptical.banmanager.lang.messages.DefaultBan;
import org.cryptical.banmanager.lang.messages.DefaultKick;
import org.cryptical.banmanager.lang.messages.DefaultMute;
import org.cryptical.banmanager.lang.messages.DefaultTempmute;
import org.cryptical.banmanager.utils.Database;
import org.cryptical.banmanager.utils.Methods;
import org.cryptical.banmanager.utils.Punisher;
import org.cryptical.banmanager.utils.Punisher.PunishReason;
import org.cryptical.banmanager.utils.serializers.DateUtils;

import net.md_5.bungee.api.ChatColor;

public class CPlayer {
	
	private UUID uuid;
	private Player player;
	private Language language = Core.languages.getLang(Core.config.getString("settings.default_language"));
	private String name = "";
	private String reason = Methods.inColors(language.getMessage(new DefaultBan()));
	private String bannedBy = "";
	private PunishReason lastPunishReason = null;

	private boolean banned = false; 
	private boolean muted = false;
	private boolean banpermanent = false;
	private boolean mutepermanent = false;
	private boolean blacklisted = false;
	private Date bannedtill, mutedtill;
	private int timesbanned = 0;
	
	public CPlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		this.name = player.getName();
	}
	
	public CPlayer(UUID uuid) {
		this.uuid = uuid;
		if (Bukkit.getPlayer(uuid) != null) {
			this.player = Bukkit.getPlayer(uuid);
			this.name = player.getName();
		}
	}
	
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	public boolean isBanned() {
		return this.banned;
	}
	
	public boolean isPermBanned() {
		return this.banpermanent;
	}
	
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
	public void setPermBanned(boolean permanent) {
		this.banpermanent = permanent;
	}
	
	public boolean isMuted() {
		return this.muted;
	}
	
	public boolean isPermMuted() {
		return this.mutepermanent;
	}
	
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	public void setPermMuted(boolean permanent) {
		this.mutepermanent = permanent;
	}
	
	public void update() {
		Date now = new Date();
		if (!banpermanent) {
			if (banned) {
				if (now.after(bannedtill)) {
					Punisher.unban(this);
					lastPunishReason = PunishReason.UNBAN;
					reason = "N/A";

					Bukkit.getPluginManager().callEvent(new PlayerUnbanEvent(this));
					Bukkit.getPluginManager().callEvent(new PlayerPunishedEvent(this, PunishReason.UNBAN));
				}
			}
		}
		if (!mutepermanent) {
			if (muted) {
				if (now.after(mutedtill)) { 
					Punisher.unmute(this);
					lastPunishReason = PunishReason.UNMUTE;
					reason = "N/A";

					Bukkit.getPluginManager().callEvent(new PlayerUnmuteEvent(this));
					Bukkit.getPluginManager().callEvent(new PlayerPunishedEvent(this, PunishReason.UNMUTE));
				}
			}
		}
		if (Bukkit.getPlayer(uuid) != null) {
			this.player = Bukkit.getPlayer(uuid);
			this.name = player.getName();
		}
	}
	
	public void setBannedTill(Date date) {
		this.bannedtill = date;
	}
	
	public Date getBannedTill() {
		return this.bannedtill;
	}
	
	public void setMutedTill(Date date) {
		this.mutedtill = date;
	}
	
	public Date getMutedTill() {
		return this.mutedtill;
	}
	
	public boolean isOnline() {
		return (this.player == null ? false : this.player.isOnline());
	}
	
	public int timesBanned() {
		return this.timesbanned;
	}
	
	public void resetBans() {
		this.timesbanned = 0;
	}
	
	public int addBan() {
		this.timesbanned += 1;
		return this.timesbanned;
	}
	
	public void setBans(int bans) {
		this.timesbanned = bans;
	}
	
	public boolean isBlacklisted() {
		return this.blacklisted;
	}
	
	public void setBlacklisted(boolean blacklisted) {
		this.blacklisted = blacklisted;
	}
	
	public void setReason(String reason) {
		this.reason = ChatColor.stripColor(reason);
	}
	
	public String getReason() {
		if (reason == null) reason = Methods.inColors(this.language.getMessage(new DefaultBan()));
		return ChatColor.stripColor(this.reason);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void ban(String reason) {
		kick(reason);
	}
	
	public void tempban(String reason) {
		kick(reason);
	}
	
	public void kick() {
		kick(Methods.inColors(this.language.getMessage(new DefaultKick())));
	}
	
	public void kick(String reason) {
		if(isOnline()) getPlayer().kickPlayer(Methods.inColors(reason));
	}
	
	public void mute() {
		mute(Methods.inColors(this.language.getMessage(new DefaultMute())));
	}
	
	public void mute(String reason) {
		if(player.isOnline()) player.sendMessage(Methods.inColors(reason));
	}
	
	public void tempmute() {
		tempmute(Methods.inColors(this.language.getMessage(new DefaultTempmute())));
	}
	
	public void tempmute(String reason) {
		mute(reason);
	}
	
	public void sendMessage(String message) {
		if(isOnline()) player.sendMessage(Methods.inColors(message));
	}
	
	public void reset() {
		setBannedTill(new Date());
		setMutedTill(new Date());
	}
	
	public Language getLanguage() {
		return this.language;
	}
	
	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public void setBannedBy(String name) {
		this.bannedBy = name;
	}
	
	public String getBannedBy() {
		return this.bannedBy;
	}
	
	public PunishReason getLastPunishReason() {
		return this.lastPunishReason;
	}
	
	public void setLastPunishReason(int id) {
		this.lastPunishReason = PunishReason.getReason(id);
	}
	
	public void log(PunishReason punishReason) {
		if (punishReason != null) this.lastPunishReason = punishReason;
		if (Core.config.getBoolean("database.logtable")) {
			boolean inprogress = getBannedTill().after(new Date());
			String reason = "N/A";
			String till = "N/A";
			if (isBanned()) {
				reason = getReason();
				till = isPermBanned() ? "permanent" : DateUtils.format(getBannedTill());
			}
			
			try {
				PreparedStatement update = Database.getConnection().prepareStatement(
						"INSERT INTO " + Database.logtable + " SET UUID=?, NAME=?, BANNEDBY=?, ETAT=?, TYPE=?, REASON=?, BANNEDTILL=?, BANNEDON=?"
					);
				update.setString(1, getUniqueId().toString());
				update.setString(2, getName());
				update.setString(3, bannedBy);
				update.setInt(4, inprogress ? 1 : 0);
				update.setLong(5, (lastPunishReason != null) ? lastPunishReason.getID() : 0);
				update.setString(6, reason);
				update.setString(7, till);
				update.setString(8, DateUtils.format(new Date()));
				update.executeUpdate();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
