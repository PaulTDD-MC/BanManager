package org.cryptical.guiapi;

import java.util.ArrayList;
import java.util.List;

public class GuiAPI {
	
	private List<CInventory> invs;
	
	public GuiAPI() {
		invs = new ArrayList<>();
	}
	
	public List<CInventory> getInventories() {
		return this.invs;
	}
	
	public CInventory createInventory(int pages, String title, int rows) {
		if (pages == 0) pages = 1;
		CInventory inv = new CInventory(invs.size(), pages, title, rows);
		invs.add(inv);
		return inv;
	}
	
	public void setParent(CInventory parent, CInventory daughter) {
		daughter.setParent(parent);
		parent.addDaughter(daughter);
	}
	
	public void setParent(CInventory parent, List<CInventory> daughters) {
		parent.setDaughters(daughters);
		for (CInventory d : daughters) {
			d.setParent(parent);
		}
	}

	public CInventory getInventory(int id) {
		for (CInventory inv : invs) {
			if (inv.getID() == id) return inv;
		} return null;
	}
}
