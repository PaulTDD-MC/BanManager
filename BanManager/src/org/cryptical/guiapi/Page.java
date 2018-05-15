package org.cryptical.guiapi;

public class Page {
	
	private int page;
	private GUI gui;
	
	protected Page(int page, int size, String title) {
		this.page = page;
		this.gui = new GUI(size, title);
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public int getPage() {
		return this.page;
	}
}
