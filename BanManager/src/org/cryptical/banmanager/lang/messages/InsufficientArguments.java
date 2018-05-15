package org.cryptical.banmanager.lang.messages;

public class InsufficientArguments extends Message {
	private String target = "none";
	
	public InsufficientArguments() {}
	
	public String getPath() {
		return "insufficient-arguments";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
