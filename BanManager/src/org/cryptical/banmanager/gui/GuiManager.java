package org.cryptical.banmanager.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.player.CPlayer;
import org.cryptical.banmanager.utils.Methods;
import org.cryptical.banmanager.utils.serializers.DateUtils;
import org.cryptical.guiapi.CInventory;
import org.cryptical.guiapi.GUI;
import org.cryptical.guiapi.GuiAPI;
import org.cryptical.guiapi.Page;

public class GuiManager {

	private GuiAPI api = Core.guiApi;
	private CInventory home, users, mutes, bans, lang;
	
	public enum GuiType {
		home(Core.config.getString("gui.main.title")), 
		users(Core.config.getString("gui.users.title")),
		bans(Core.config.getString("gui.bans.title")),
		mutes(Core.config.getString("gui.mutes.title")),
		lang(Core.config.getString("gui.lang.title"));
		
		private String title;
		GuiType(String title) {
			this.title = title;
		}
		
		public String getName() {
			return this.title;
		}
		
		public static GuiType getType(String type) {
			if (type.equals("home")) return GuiType.home;
			if (type.equals("users")) return GuiType.users;
			if (type.equals("mutes")) return GuiType.mutes;
			if (type.equals("bans")) return GuiType.bans;
			if (type.equals("lang")) return GuiType.lang;
			return null;
		}
	}
	
	public GuiManager() {
		setup();
	}
	
	public void setup() {
		home = api.createInventory(1, Methods.inColors(GuiType.home.getName()), 5);
		users = api.createInventory( (int)Math.ceil((double)Core.playerData.getPlayers().size() / 21.0), Methods.inColors(GuiType.users.getName()), 5);
		mutes = api.createInventory( (int)Math.ceil((double)Core.playerData.getMutedPlayers().size() / 21.0), Methods.inColors(GuiType.mutes.getName()), 5);
		bans = api.createInventory( (int)Math.ceil((double)Core.playerData.getBannedPlayers().size() / 21.0), Methods.inColors(GuiType.bans.getName()), 5);
		lang = api.createInventory(1, Methods.inColors(GuiType.lang.getName()), 5);
		
		api.setParent(home, Arrays.asList(users,mutes,bans));

		clearAll();
		fill();
		setupPages();
	}
	
	public void fill() {
		for (GuiType type : GuiType.values()) {
			createDefault(getInventory(type));
			CInventory _inv = getInventory(type);
			Page[] pages = _inv.getPages();
			
			if (type != GuiType.home) {
				if (_inv.getParent() != null) 
					pages[0].getGUI().getInventory().setItem(36, Methods.getSkull(1, Methods.inColors(Core.config.getString("gui.items.previous.name")), Methods.inColors(Core.config.getStringList("gui.items.previous.lore")), "MHF_ArrowLeft"));
				if (pages.length > 1) {
					pages[pages.length-1].getGUI().getInventory().setItem(36, Methods.getSkull(1, Methods.inColors(Core.config.getString("gui.items.previous.name")), Methods.inColors(Core.config.getStringList("gui.items.previous.lore")), "MHF_ArrowLeft"));
					for (int i = 1; i < pages.length-2; i++) {
						pages[i].getGUI().getInventory().setItem(36, Methods.getSkull(1, Methods.inColors(Core.config.getString("gui.items.previous.name")), Methods.inColors(Core.config.getStringList("gui.items.previous.lore")), "MHF_ArrowLeft"));
						pages[i].getGUI().getInventory().setItem(44, Methods.getSkull(1, Methods.inColors(Core.config.getString("gui.items.next.name")), Methods.inColors(Core.config.getStringList("gui.items.next.lore")), "MHF_ArrowRight"));
					}
				}
			}
		}
	}
	
	public CInventory getInventory(GuiType gui) {
		switch(gui) {
		case home: return this.home;
		case users: return this.users;
		case bans: return this.bans;
		case mutes: return this.mutes;
		case lang: return this.lang;
		} return null;
	}
	
	public Inventory getInventory(CInventory inv, int page) {
		Page[] pages = inv.getPages();
		int p = (page -1);
		if (p >= pages.length) page = pages.length-1;
		
		Page _page = pages[p];
		return _page.getGUI().getInventory();
	}
	
	public void createDefault(CInventory inv) {
		for (Page page : inv.getPages()) {
			int[] i1 = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};
			for (int i : i1) {
				page.getGUI().getInventory().setItem(i, Methods.getItem(Material.STAINED_GLASS_PANE, 15, 1, " ", new ArrayList<>()));
			}
		}
	}
	
	public void clearAll() {
		for (GuiType type : GuiType.values()) {
			for (Page page : getInventory(type).getPages()) {
				page.getGUI().getInventory().clear();
			}
		}
	}
	
	public void setupPages() {
		setupHome();
		setupUsers();
		setupBans();
		setupMutes();
		setupLang();
	}
	
	public void setupHome() {
		Page[] pages = home.getPages();
		GUI gui = pages[0].getGUI();
		
		for (String s : Core.config.getConfigurationSection("gui.main.items").getKeys(false)) {
			String path = "gui.main.items."+s;
			int slot = Core.config.getInt(path+".slot");
			String name = Methods.inColors(Core.config.getString(path+".name"));
			List<String> lore = Methods.inColors(Core.config.getStringList(path + ".lore"));
			int amount = Core.config.getInt(path + ".amount");
			int damage = Core.config.getInt(path + ".damage");
			Material material = Material.valueOf(Core.config.getString(path + ".material"));
			
			gui.getInventory().setItem(slot, Methods.getItem(material, damage, amount, name, lore));
		}
	}
	
	public void setupUsers() {
		Page[] pages = users.getPages();

		for (Page page : pages) {
			int _page = page.getPage();
			
			for (int i = _page*21; i < Core.playerData.getPlayers().size(); i++) {
				CPlayer target = Core.playerData.getPlayers().get(i);
				List<String> lore = Arrays.asList(
						" §7Banned: §9" + target.isBanned(),
						" §7Permbanned: §9" + target.isPermBanned(),
						" §7Muted: §9" + target.isMuted(),
						" §7Permmuted: §9" + target.isPermMuted(),
						" §7Times punished: §9" + target.timesBanned(),
						" §7Banned till: §9" + (target.isBanned() ? (target.isPermBanned() ? "permanent" : DateUtils.format(target.getBannedTill())) : "N/A"),
						" §7Muted till: §9" + (target.isMuted() ? (target.isPermMuted() ? "permanent" : DateUtils.format(target.getMutedTill())) : "N/A"),
						" §7Ban reason: §9" + (target.isBanned() ? target.getReason() : "none"),
						" §7Banned by: §9" + (target.isBanned() ? target.getBannedBy() : "N/A"));
				page.getGUI().getInventory().addItem(Methods.getSkull(1, "§8"+target.getName(), lore, target.getName()));
			}
		}
	}
	
	public void setupBans() {
		Page[] pages = bans.getPages();
		
		for (Page page : pages) {
			int _page = page.getPage();

			for (int i = _page*21; i < Core.playerData.getBannedCPlayers().size(); i++) {
				CPlayer target = Core.playerData.getBannedCPlayers().get(i);
				List<String> lore = Arrays.asList(
						" §7Permbanned: §9" + target.isPermBanned(),
						" §7Banned till: §9" + (target.isBanned() ? (target.isPermBanned() ? "permanent" : DateUtils.format(target.getBannedTill())) : "N/A"),
						" §7Ban reason: §9" + (target.isBanned() ? target.getReason() : "none"),
						" §7Banned by: §9" + (target.isBanned() ? target.getBannedBy() : "N/A"));
				page.getGUI().getInventory().addItem(Methods.getSkull(1, "§8"+target.getName(), lore, target.getName()));
			}
		}
	}
	
	public void setupMutes() {
		Page[] pages = mutes.getPages();
		
		for (Page page : pages) {
			int _page = page.getPage();
			
			for (int i = _page*21; i < Core.playerData.getMutedCPlayers().size(); i++) {
				CPlayer target = Core.playerData.getMutedCPlayers().get(i);
				List<String> lore = Arrays.asList(
						" §7Permmuted: §9" + target.isPermMuted(),
						" §7Muted till: §9" + (target.isMuted() ? (target.isPermMuted() ? "permanent" : DateUtils.format(target.getMutedTill())) : "N/A"),
						" §7Muted by: §9" + (target.isBanned() ? target.getBannedBy() : "N/A"));
				page.getGUI().getInventory().addItem(Methods.getSkull(1, "§8"+target.getName(), lore, target.getName()));
			}
		}
	}
	
	public void setupLang() {
		Page[] pages = lang.getPages();
		GUI gui = pages[0].getGUI();
		
		for (String s : Core.config.getConfigurationSection("gui.lang.items").getKeys(false)) {
			String path = "gui.lang.items."+s;
			int slot = Core.config.getInt(path+".slot");
			String name = Methods.inColors(Core.config.getString(path+".name"));
			List<String> lore = Methods.inColors(Core.config.getStringList(path + ".lore"));
			int amount = Core.config.getInt(path + ".amount");
			int damage = Core.config.getInt(path + ".damage");
			Material material = Material.valueOf(Core.config.getString(path + ".material"));
			
			gui.getInventory().setItem(slot, Methods.getItem(material, damage, amount, name, lore));
		}
	}
}
