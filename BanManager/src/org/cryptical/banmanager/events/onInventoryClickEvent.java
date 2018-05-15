package org.cryptical.banmanager.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.gui.GuiManager.GuiType;
import org.cryptical.banmanager.lang.languages.Language;
import org.cryptical.banmanager.lang.messages.LanguageChanged;
import org.cryptical.banmanager.lang.messages.LanguageInvalid;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.Methods;
import org.cryptical.guiapi.CInventory;
import org.cryptical.guiapi.Page;

public class onInventoryClickEvent implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		Player player = (Player)e.getWhoClicked();
		Inventory inventory = e.getInventory();
		String title = inventory.getTitle();
		int slot = e.getSlot();
		CPlayer cplayer = Core.playerData.getPlayer(player.getUniqueId());
		
		CInventory inv = null;
		GuiType type = null;
		for (GuiType t : GuiType.values()) {
			String name = Methods.inColors(t.getName());
			if (name.equals(title)) {
				inv = Core.guiManager.getInventory(t);
				type = t;
			}
		}
		
		if (inv == null || type == null) return;
		
		e.setCancelled(true);
		if (type == GuiType.home) {
			String item = "";
			for (String s : Core.config.getConfigurationSection("gui.main.items").getKeys(false)) {
				if (Core.config.getInt("gui.main.items."+s+".slot") == e.getSlot()) item = s;
			}
			if (item.equals("")) return;
			if (item.equals("bans")) player.openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(GuiType.bans), 1));
			if (item.equals("mutes")) player.openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(GuiType.mutes), 1));
			if (item.equals("users")) player.openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(GuiType.users), 1));
		}
		
		if (type == GuiType.lang) {
			String item = "";
			for (String s : Core.config.getConfigurationSection("gui.lang.items").getKeys(false)) {
				if (Core.config.getInt("gui.lang.items."+s+".slot") == e.getSlot()) item = s;
			}
			Language lang = Core.languages.getLang(item.toLowerCase());
			if (lang == null) {
				player.sendMessage(cplayer.getLanguage().getMessage(new LanguageInvalid()));
				player.closeInventory();
				return;
			}
			cplayer.setLanguage(lang);
			player.sendMessage(cplayer.getLanguage().getMessage(new LanguageChanged(lang.getLanguageName())));
			player.closeInventory();
		}
		
		else {
			Page _page = null;
			for (Page p : inv.getPages()) {
				if (e.getInventory().equals(p.getGUI().getInventory())) {
					_page = p;
				}
			}
			if (_page == null) return;
			int page = _page.getPage()+1;
			if (slot == 36) {
				if (page > 1) {
					player.openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(type), page-1));
				}
				else if (page == 1) {
					player.openInventory(inv.getParent().getPages()[0].getGUI().getInventory());
				}
			}
			else if (slot == 44) {
				if (page < inv.getPages().length) {
					player.openInventory(Core.guiManager.getInventory(Core.guiManager.getInventory(type), page+1));
				}
			}
		}
	}
}
