package org.cryptical.banmanager.utils;

import java.util.Date;

import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.lang.messages.DefaultBan;
import org.cryptical.banmanager.lang.messages.DefaultKick;
import org.cryptical.banmanager.lang.messages.DefaultTempban;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.serializers.DateUtils;

import net.md_5.bungee.api.ChatColor;

public class Punisher {
	
	public enum PunishReason {
		KICK(1), MUTE(2), BAN(3), TEMPBAN(4), TEMPMUTE(5), PUNISH(6), UNBAN(7), UNMUTE(8);
		
		private int id;
		PunishReason(int id) {
			this.id = id;
		}
		
		public int getID() {
			return this.id;
		}
		
		public static PunishReason getReason(int id) {
			if (id == 1) return PunishReason.KICK;
			if (id == 2) return PunishReason.MUTE;
			if (id == 3) return PunishReason.BAN;
			if (id == 4) return PunishReason.TEMPBAN;
			if (id == 5) return PunishReason.TEMPMUTE;
			if (id == 6) return PunishReason.UNBAN;
			if (id == 7) return PunishReason.UNMUTE;
			return null;
 		}
		
		public static PunishReason getReason(String reason) {
			if (reason.equals("kick")) return PunishReason.KICK;
			if (reason.equals("mute")) return PunishReason.MUTE;
			if (reason.equals("ban")) return PunishReason.BAN;
			if (reason.equals("tempban")) return PunishReason.TEMPBAN;
			if (reason.equals("tempmute")) return PunishReason.TEMPMUTE;
			return null;
		}
	}
	
	// methods
	public static void ban(CPlayer player) {
		player.setBanned(true);
		player.setPermBanned(true);
		player.setReason(Methods.inColors(player.getLanguage().getMessage(new DefaultBan())));
		player.ban(banmsg(player, player.getLanguage().getMessage(new DefaultBan())));
	}
	
	public static void ban(CPlayer player, String reason) {
		player.setBanned(true);
		player.setPermBanned(true);
		player.setReason(reason);
		player.ban(banmsg(player, reason));
	}
	
	public static void tempban(CPlayer player, Date till) {
		player.setBanned(true);
		player.setPermBanned(false);
		player.setBannedTill(till);
		player.setReason(Methods.inColors(player.getLanguage().getMessage(new DefaultBan())));
		player.tempban(banmsg(player, player.getLanguage().getMessage(new DefaultTempban())));
	}
	
	public static void tempban(CPlayer player, Date till, String reason) {
		player.setBanned(true);
		player.setPermBanned(false);
		player.setBannedTill(till);
		player.setReason(reason);
		player.tempban(banmsg(player, reason));
	}
	
	public static void unban(CPlayer player) {
		player.setBanned(false);
		player.reset();
		player.setPermBanned(false);
	}
	
	public static void mute(CPlayer player) {
		player.setMuted(true);
		player.mute();
		player.setPermMuted(true);
	}
	
	public static void mute(CPlayer player, String reason) {
		player.setMuted(true);
		player.mute(reason);
		player.setPermMuted(true);
	}
	
	public static void tempmute(CPlayer player, Date till) {
		player.setMuted(true);
		player.setMutedTill(till);
		player.tempmute();
		player.setPermMuted(false);
	}
	
	public static void tempmute(CPlayer player, Date till, String reason) {
		player.setMuted(true);
		player.setMutedTill(till);
		player.tempmute(reason);
		player.setPermMuted(false);
	}
	
	public static void unmute(CPlayer player) {
		player.setMuted(false);
		player.reset();
		player.setPermMuted(false);
	}
	
	public static void kick(CPlayer player) {
		player.kick(kickmsg(player, null));
	}
	
	public static void kick(CPlayer player, String reason) {
		player.kick(kickmsg(player, reason));
	}
	
	private static String banmsg(CPlayer player, String reason) {
		String banmessage = "";
		for (String s : player.getLanguage().getConf().getStringList("ban-message")) {
			String line = s.replace("%reason%", ChatColor.stripColor(reason));
			line = line.replace("%till%", (player.isPermBanned() ? "never" : DateUtils.format(player.getBannedTill())));
			line = line.replace("%bannedby%", player.getBannedBy());
			
			banmessage += line + "\n";
		} return banmessage;
	}
	
	private static String kickmsg(CPlayer player, String reason) {
		String s = reason;
		if (reason == null) s = player.getLanguage().getMessage(new DefaultKick());
		
		String message = "";
		for (String l : player.getLanguage().getConf().getStringList("kick-message")) {
			String line = l.replace("%reason%", ChatColor.stripColor(s));
			message += line + "\n";
		} 	return message;
	}
	
	public static void punish(CPlayer player) {
		int bans = player.timesBanned();
		int next = bans+1;
		if (next > getMaxPunishes()) next = bans;
		
		for (String r : Core.config.getStringList("settings.punish."+next)) {
			String[] s = r.split(",");
			
			Date till = null;
			if (s.length == 2) {
				int[] i = DateUtils.subtract(s[1]);
				till = DateUtils.add(new Date(), i[0], i[1], i[2], i[3], i[4]);
			}
			PunishReason reason = PunishReason.getReason(s[0]);
			if (reason == PunishReason.KICK) kick(player);
			if (reason == PunishReason.MUTE) mute(player);
			if (reason == PunishReason.BAN) ban(player);
			if (reason == PunishReason.TEMPBAN) tempban(player, till);
			if (reason == PunishReason.TEMPMUTE) tempmute(player, till);
		}
		player.addBan();
	}
	
	public static void punish(CPlayer player, String sreason) {
		int bans = player.timesBanned();
		int next = bans+1;
		if (next > getMaxPunishes()) next = bans;

		for (String r : Core.config.getStringList("settings.punish."+next)) {
			String[] s = r.split(",");
			
			Date till = null;
			if (s.length == 2) {
				int[] i = DateUtils.subtract(s[1]);
				till = DateUtils.add(new Date(), i[0], i[1], i[2], i[3], i[4]);
			}
			PunishReason reason = PunishReason.getReason(s[0]);
			if (reason == PunishReason.KICK) kick(player, sreason);
			if (reason == PunishReason.MUTE) mute(player, sreason);
			if (reason == PunishReason.BAN) ban(player, sreason);
			if (reason == PunishReason.TEMPBAN) tempban(player, till, sreason);
			if (reason == PunishReason.TEMPMUTE) tempmute(player, till, sreason);
		}
		player.addBan();
	}
	
	public static int getMaxPunishes() {
		return Core.config.getConfigurationSection("settings.punish").getKeys(false).size();
	}
}
