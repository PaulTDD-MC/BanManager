package org.cryptical.banmanager.lang.messages;

public class BanlistEmpty extends Message {
	private String target = "none";
	
	public BanlistEmpty() {}
	
	public String getPath() {
		return "banlist-empty";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
