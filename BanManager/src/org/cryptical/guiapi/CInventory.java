package org.cryptical.guiapi;

import java.util.List;

public class CInventory {
	
	private Page[] pages;
	private String title;
	private int id;
	private CInventory parent = null;
	private List<CInventory> daughters = null;
	
	public CInventory(int id, int pages, String title, int rows) {
		this.id = id;
		this.title = title;
		this.pages = new Page[pages];
		
		for (int i = 0; i < this.pages.length; i++) {
			this.pages[i] = new Page(i, ((rows*9)), title);
		}
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Page[] getPages() {
		return this.pages;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setParent(CInventory parent) {
		this.parent = parent;
	}
	
	public void addDaughter(CInventory daughter) {
		if (!this.daughters.contains(daughter)) this.daughters.add(daughter);
	}
	
	public void removeDaughter(CInventory daughter) {
		if (this.daughters.contains(daughter)) this.daughters.remove(daughter);
	}
	
	public void setDaughters(List<CInventory> daughters) {
		this.daughters = daughters;
	}
	
	public CInventory getParent() {
		return this.parent;
	}
	
	public List<CInventory> getDaughters() {
		return this.daughters;
	}
}
