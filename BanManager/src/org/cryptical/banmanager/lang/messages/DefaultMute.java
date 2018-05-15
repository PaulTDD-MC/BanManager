package org.cryptical.banmanager.lang.messages;

public class DefaultMute extends Message {
	private String target = "none";
	
	public DefaultMute() {}
	
	public String getPath() {
		return "default-mute";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
