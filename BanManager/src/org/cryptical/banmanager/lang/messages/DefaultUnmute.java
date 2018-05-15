package org.cryptical.banmanager.lang.messages;

public class DefaultUnmute extends Message {
	private String target = "none";
	
	public DefaultUnmute() {}
	
	public String getPath() {
		return "default-unmute";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
