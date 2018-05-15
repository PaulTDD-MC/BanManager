package org.cryptical.banmanager.lang.messages;

public class LanguageSet extends Message {
	private String target = "none";
	private String language = "none";
	
	public LanguageSet(String target, String language) {
		this.target = target;
		this.language = language;
	}
	public String getPath() {
		return "language-set";
	}
	public String getTarget() {
		return target;
	}
	public String getLanguage() {
		return this.language;
	}
}
