package org.cryptical.guiapi;

public class MainGui {
	
	private static CHolder holder;
	private GuiAPI api;
	
	public MainGui() {
		holder = new CHolder();
		api = new GuiAPI();
	}

	public static CHolder getHolder() {
		return holder;
	}
	
	public GuiAPI get() {
		return api;
	}
}
