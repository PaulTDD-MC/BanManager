package org.cryptical.banmanager.lang.messages;

public class PlayerNotFound extends Message {
	private String target = "none";
	
	public PlayerNotFound(String target) {
		this.target = target;
	}
	public String getPath() {
		return "player-not-found";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
