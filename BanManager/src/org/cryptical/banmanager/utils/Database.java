package org.cryptical.banmanager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.serializers.BooleanUtils;

public class Database {
	
	public static String host = Core.config.getString("database.host");
	public static String database = Core.config.getString("database.database");
	public static String username = Core.config.getString("database.user");
	public static String password = Core.config.getString("database.password");
	public static int port = Core.config.getInt("database.port");
	public static Connection con;
	
	public static String table = "BanManager_data";
	public static String logtable = "BanManager_log";
	
	static ConsoleCommandSender console = Bukkit.getConsoleSender();
	
	// connect
	public static void connect() throws SQLException {
		if (!isConnected()) {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		}
	}
	
	//disconnect
	public static void disconnect() {
		if (isConnected()) {
			try {
				con.close();
				console.sendMessage("["+Core.pl.getDescription().getName()+"] " + ChatColor.GREEN + "Disconnecting from the MySQL database...");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createFirstTables() {
		dataTable();
		if(Core.config.getBoolean("database.logtable")) {
			logTable();
		}
	}
	
	private static void dataTable() {
		PreparedStatement ps = null;
		try {
			ps = Database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" (id INT(255) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id), UUID varchar(255), NAME varchar(255), BANNED INT(1), MUTED INT(1), PERMBAN INT(1), PERMMUTE INT(1), TIMESBANNED INT(1), BANNEDTILL varchar(255), MUTEDTILL varchar(255), BANNEDBY varchar(255), REASON varchar(255), LANGUAGE varchar(255))");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (Core.config.getBoolean("database.upgrade_table")) {
			console.sendMessage("["+Core.pl.getDescription().getName()+"] " + ChatColor.GREEN + "Trying to update the old table");
			try {
				PreparedStatement rename = Database.getConnection().prepareStatement("RENAME TABLE "+ table + " TO " + table + "_old");
				rename.executeUpdate();
				
				ps.executeUpdate();
				
				PreparedStatement copy = Database.getConnection().prepareStatement(
						"INSERT INTO "+ table + " (`id`, `UUID`, `NAME`, `BANNED`, `MUTED`, `PERMBAN`, `PERMMUTE`, `TIMESBANNED`, `BANNEDTILL`, `MUTEDTILL`, `REASON`) "
						+ "SELECT `id`, `UUID`, `NAME`, `BANNED`, `MUTED`, `PERMBAN`, `PERMMUTE`, `TIMESBANNED`, `BANNEDTILL`, `MUTEDTILL`, `REASON` FROM " + table + "_old");
				copy.executeUpdate();
				
				PreparedStatement lang = Database.getConnection().prepareStatement("UPDATE " + table + " SET LANGUAGE = '" + Core.config.getString("settings.default_language") +"'");
				lang.executeUpdate();
				
				PreparedStatement drop = Database.getConnection().prepareStatement("DROP TABLE " + table + "_old");
				drop.executeUpdate();
				
				console.sendMessage("["+Core.pl.getDescription().getName()+"] " + ChatColor.GREEN + "Updated old table");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private static void logTable() {
		try {
			PreparedStatement create = Database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + logtable + " (id INT(255) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id), UUID varchar(255), NAME varchar(255), BANNEDBY varchar(255), ETAT INT(1), TYPE INT(1), REASON VARCHAR(255), BANNEDTILL VARCHAR(255), BANNEDON VARCHAR(255))");
			create.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void put(Player p, String table, String string, String value) {
		
		PreparedStatement ps;
		try {
			ps = Database.getConnection().prepareStatement("INSERT INTO " + table + "(UUID," + string + ") VALUES (?,?)");
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, value);
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String get(String table, String result, String where, String value) throws SQLException {
		
		PreparedStatement ps;
		ps = Database.getConnection().prepareStatement("SELECT " + result + " FROM " + table + " WHERE " + where + " = ?");
		ps.setString(1, value);

		ResultSet rs = ps.executeQuery();

		String string = "";
		if (rs.next()) {
			string = rs.getString("" + result);
		}
		return string;
	}
	
	public static void update(Player p, String table, String string, String value) {
		
		PreparedStatement ps;
		try {
			ps = Database.getConnection().prepareStatement("UPDATE " + table + " SET " + string + "=? WHERE UUID=?");
			ps.setString(1, value);
			ps.setString(2, p.getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// uuid banned muted timesbanned
	public static void updateAll(CPlayer p, String table, boolean banned, boolean muted, boolean permban, boolean permmute, int timesbanned, String bannedtill, String mutedtill, String reason, String bannedby, String language) {
		PreparedStatement ps;
		try {
			ps = Database.getConnection().prepareStatement("UPDATE " + table + " SET NAME=?, BANNED=?, MUTED=?, PERMBAN=?, PERMMUTE=?, TIMESBANNED=?, BANNEDTILL=?, MUTEDTILL=?, REASON=?, BANNEDBY=?, LANGUAGE=? WHERE UUID=?");
			ps.setString(1, p.getName());
			ps.setInt(2, BooleanUtils.toInt(banned));
			ps.setInt(3, BooleanUtils.toInt(muted));
			ps.setInt(4, BooleanUtils.toInt(permban));
			ps.setInt(5, BooleanUtils.toInt(permmute));
			ps.setInt(6, timesbanned);
			ps.setString(7, bannedtill);
			ps.setString(8, mutedtill);
			ps.setString(9, reason);
			ps.setString(10, bannedby);
			ps.setString(11, language);
			ps.setString(12, p.getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isConnected() {
		return (con == null ? false : true);
	}
	
	public static Connection getConnection() {
		return con;
	}
	
	public static boolean contains(String table, Player p) {
		try {
			String s = get(table, "name", "uuid", p.getUniqueId().toString());
			if (s.equals("")) return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
