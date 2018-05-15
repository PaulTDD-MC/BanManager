package org.cryptical.banmanager.lang;

import java.util.HashMap;

import org.cryptical.banmanager.Core;
import org.cryptical.banmanager.lang.languages.Language;

public class LangSetup {
	
	private HashMap<String, Language> languages = new HashMap<>();
	
	public LangSetup() {
		for (String tag : Core.config.getConfigurationSection("settings.languages").getKeys(false)) {
			languages.put(tag, new Language(tag, Core.config.getString("settings.languages."+tag)));
		}
	}
	
	public Language getLang(String name) {
		for (Language l : languages.values()) {
			if(l.getLanguageName().toLowerCase().equals(name.toLowerCase())) {
				return l;
			}
		} return null;
	}
	
	public HashMap<String, Language> get() {
		return this.languages;
	}
}
