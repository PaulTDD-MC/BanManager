package org.cryptical.banmanager.lang.messages;

public class DefaultTempban extends Message {
	private String target = "none";
	
	public DefaultTempban() {}
	
	public String getPath() {
		return "default-tempban";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
