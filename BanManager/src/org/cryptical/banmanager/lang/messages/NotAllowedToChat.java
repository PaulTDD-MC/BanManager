package org.cryptical.banmanager.lang.messages;

public class NotAllowedToChat extends Message {
	private String target = "none";
	
	public NotAllowedToChat() {}
	
	public String getPath() {
		return "not-allowed-to-chat";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
