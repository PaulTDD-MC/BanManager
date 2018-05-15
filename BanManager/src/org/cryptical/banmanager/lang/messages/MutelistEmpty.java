package org.cryptical.banmanager.lang.messages;

public class MutelistEmpty extends Message {
	private String target = "none";
	
	public MutelistEmpty() {}
	
	public String getPath() {
		return "mutelist-empty";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
