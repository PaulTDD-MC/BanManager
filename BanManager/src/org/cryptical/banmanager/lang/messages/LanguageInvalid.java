package org.cryptical.banmanager.lang.messages;

public class LanguageInvalid extends Message {
	private String target = "none";
	
	public LanguageInvalid() {}
	public String getPath() {
		return "language-invalid";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return "";
	}
}
