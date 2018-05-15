package org.cryptical.banmanager.lang.messages;

public class CommandNotFound extends Message {
	private String target = "none";
	
	public CommandNotFound() {}
	
	public String getPath() {
		return "command-not-found";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
