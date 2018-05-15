package org.cryptical.guiapi;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class GUI {
	
	private Inventory inventory;
	
	protected GUI(int size, String title) {
		inventory = Bukkit.createInventory(MainGui.getHolder(), size, title);
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
}
