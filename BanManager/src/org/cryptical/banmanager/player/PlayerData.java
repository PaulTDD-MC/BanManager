package org.cryptical.banmanager.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.utils.Database;
import org.cryptical.banmanager.utils.serializers.BooleanUtils;
import org.cryptical.banmanager.utils.serializers.DateUtils;

public class PlayerData {

	List<CPlayer> players;
	
	public PlayerData() {
		players = new ArrayList<>();
		load();
	}
	
	public CPlayer newPlayer(UUID uuid) {
		return playerExists(uuid) ? getPlayer(uuid) : addPlayer(uuid);
	}
	
	public boolean playerExists(UUID uuid) {
		for (CPlayer player : getPlayers()) {
			if(player.getUniqueId().equals(uuid)) return true;
		} return false;
	}
	
	private CPlayer addPlayer(UUID uuid) {
		CPlayer player = new CPlayer(uuid);
		if (!players.contains(player)) players.add(player);
		return player;
	}
	
	public CPlayer getPlayer(UUID uuid) {
		for (CPlayer player : getPlayers()) {
			if (player.getUniqueId().equals(uuid)) return player;
		} return null;
	}
	
	public CPlayer getPlayer(String name) {
		for (CPlayer player : getPlayers()) {
			if(player.getName().toLowerCase().equals(name.toLowerCase())) {
				return player;
			}
		} return null;
	}
	
	public List<CPlayer> getOnlinePlayers() {
		List<CPlayer> list = new ArrayList<>();
		for (CPlayer player : getPlayers()) {
			if (player.isOnline()) list.add(player);
		} return list;
	}
	
	public List<CPlayer> getPlayers() {
		return this.players;
	}
	
	public List<CPlayer> getBannedCPlayers() {
		List<CPlayer> list = new ArrayList<>();
		for (CPlayer player : getPlayers()) {
			if (player.isBanned()) list.add(player);
		} return list;
	}
	
	public List<Player> getBannedPlayers() {
		List<Player> list = new ArrayList<>();
		for (CPlayer player : getBannedCPlayers()) {
			list.add(player.getPlayer());
		} return list;
	}
	
	public List<CPlayer> getMutedCPlayers() {
		List<CPlayer> list = new ArrayList<>();
		for (CPlayer player : getPlayers()) {
			if (player.isMuted()) list.add(player);
		} return list;
	}
	
	public List<Player> getMutedPlayers() {
		List<Player> list = new ArrayList<>();
		for (CPlayer player : getMutedCPlayers()) {
			list.add(player.getPlayer());
		} return list;
	}
	
	public void save() {
		Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_AQUA + "Saved playerdata");
		if (Database.isConnected()) {
			for (CPlayer player : getPlayers()) {
				Database.updateAll(player, 
						Database.table, 
						player.isBanned(), 
						player.isMuted(), 
						player.isPermBanned(), 
						player.isPermMuted(),
						player.timesBanned(), 
						((player.getBannedTill() == null) ? "na" : DateUtils.serialize(player.getBannedTill())), 
						((player.getMutedTill() == null) ? "na" :DateUtils.serialize(player.getMutedTill())), 
						player.getReason(),
						player.getBannedBy(),
						((player.getLanguage() == null) ? Core.config.getString("settings.default_language") : player.getLanguage().getLanguageName()));
			}
			return;
		}
		
		for (CPlayer player : getPlayers()) {
			Core.userdata.set(player.getUniqueId().toString() + ".name", player.getName());
			Core.userdata.set(player.getUniqueId().toString() + ".banned", player.isBanned());
			Core.userdata.set(player.getUniqueId().toString() + ".muted", player.isMuted());
			Core.userdata.set(player.getUniqueId().toString() + ".timesbanned", player.timesBanned());
			Core.userdata.set(player.getUniqueId().toString() + ".bannedtill", DateUtils.serialize(player.getBannedTill()));
			Core.userdata.set(player.getUniqueId().toString() + ".mutedtill", DateUtils.serialize(player.getMutedTill()));
			Core.userdata.set(player.getUniqueId().toString() + ".permbanned", player.isPermBanned());
			Core.userdata.set(player.getUniqueId().toString() + ".permmuted", player.isPermMuted());
			Core.userdata.set(player.getUniqueId().toString() + ".reason", player.getReason());
			Core.userdata.set(player.getUniqueId().toString() + ".language", player.getReason());
			Core.userdata.set(player.getUniqueId().toString() + ".bannedby", player.getBannedBy());
		}
	}
	
	public void load() {
		if (Database.isConnected()) {
			PreparedStatement stat;
			try {
				stat = Database.getConnection().prepareStatement("SELECT * FROM " + Database.table);
				ResultSet set = stat.executeQuery();
				while(set.next()) {
					CPlayer player = newPlayer(UUID.fromString(set.getString("UUID")));
					player.setName(set.getString("NAME"));
					player.setBanned(BooleanUtils.fromInt(set.getInt("BANNED")));
					player.setMuted(BooleanUtils.fromInt(set.getInt("MUTED")));
					player.setPermBanned(BooleanUtils.fromInt(set.getInt("PERMBAN")));
					player.setPermMuted(BooleanUtils.fromInt(set.getInt("PERMMUTE")));
					player.setBans(set.getInt("TIMESBANNED"));
					player.setBannedTill(DateUtils.deserialize(set.getString("BANNEDTILL")));
					player.setMutedTill(DateUtils.deserialize(set.getString("MUTEDTILL")));
					player.setReason(set.getString("REASON"));
					player.setLanguage(Core.languages.getLang(set.getString("LANGUAGE")));
					player.setBannedBy(set.getString("BANNEDBY"));
					
					if (Core.config.getBoolean("database.logtable")) {
						PreparedStatement log = Database.getConnection().prepareStatement("SELECT * FROM " + Database.logtable);
						ResultSet set2 = log.executeQuery();
						while (set2.next()) {
							player.setLastPunishReason(set2.getInt("TYPE"));
						}
					}
				}
			} catch (SQLException ex) { ex.printStackTrace(); }
			Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_AQUA + "Updated player data (database)");
			return;
		}
		
		for (String s : Core.userdata.getKeys(false)) {
			UUID uuid = UUID.fromString(s);
			CPlayer player = newPlayer(uuid);
			player.setName(Core.userdata.getString(s+".name"));
			player.setBanned(Core.userdata.getBoolean(s+".banned"));
			player.setMuted(Core.userdata.getBoolean(s+".muted"));
			player.setPermBanned(Core.userdata.getBoolean(s+".permbanned"));
			player.setPermMuted(Core.userdata.getBoolean(s+".permmuted"));
			player.setBans(Core.userdata.getInt(s+".timesbanned"));
			player.setBannedTill(DateUtils.deserialize(Core.userdata.getString(s+".bannedtill")));
			player.setMutedTill(DateUtils.deserialize(Core.userdata.getString(s+".mutedtill")));
			player.setReason(Core.userdata.getString(s+".reason"));
			player.setLanguage(Core.languages.getLang(Core.userdata.getString(s+".language")));
			player.setBannedBy(Core.userdata.getString(s+".bannedby"));
		}
		Bukkit.getConsoleSender().sendMessage("[" + Core.pl.getDescription().getName() + "] " + ChatColor.DARK_AQUA + "Updated player data (file)");
	}
}
