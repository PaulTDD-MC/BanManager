package org.cryptical.banmanager.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Methods {

	public static boolean equalsCommand(String argument, List<String> commands) {
		if (commands.contains(argument))
			return true;
		return false;
	}

	public static List<String> inColors(List<String> path) {
		List<String> msg = new ArrayList<>();
		for (String message : path) {
			msg.add(ChatColor.translateAlternateColorCodes('&', message));
		}
		return msg;
	}

	public static String inColors(String path) {
		return "" + ChatColor.translateAlternateColorCodes('&', path);
	}
	
	public static ItemStack getItem(Material material, int damage, int amount, String name, List<String> lore) {
		ItemStack item = new ItemStack(material, amount, (byte)damage);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSkull(int amount, String name, List<String> lore, String owner) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte)3);
		SkullMeta meta = (SkullMeta)item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.setOwner(owner);
		item.setItemMeta(meta);
		return item;
	}
}
