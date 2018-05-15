package org.cryptical.banmanager.lang.messages;

public class DefaultKick extends Message {
	private String target = "none";
	
	public DefaultKick() {}
	
	public String getPath() {
		return "default-kick";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
