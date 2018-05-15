package org.cryptical.banmanager.lang.messages;

public class DefaultTempmute extends Message {
	private String target = "none";
	
	public DefaultTempmute() {}
	
	public String getPath() {
		return "default-tempmute";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
