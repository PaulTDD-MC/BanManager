package org.cryptical.banmanager.lang.messages;

public class DefaultBan extends Message {
	private String target = "none";
	
	public DefaultBan() {}
	
	public String getPath() {
		return "default-ban";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
