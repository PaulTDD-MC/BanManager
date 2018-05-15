package org.cryptical.banmanager.lang.messages;

public class NoPermission extends Message {
	private String target = "none";
	
	public NoPermission() {}
	
	public String getPath() {
		return "no-permission";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
