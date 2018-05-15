package org.cryptical.banmanager.lang.messages;

public class LanguageChanged extends Message {
	private String target = "none";
	private String language;
	
	public LanguageChanged(String language) {
		this.language = language;
	}
	public String getPath() {
		return "language-changed";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return language;
	}
}
