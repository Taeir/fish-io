package com.github.fishio.multiplayer.server;

import java.util.HashMap;

/**
 * ServerMessage for settings.
 */
public class FishServerSettingsMessage implements FishServerMessage {
	private static final long serialVersionUID = 8467180595977445255L;
	
	private HashMap<String, Object> settings = new HashMap<String, Object>();
	
	/**
	 * Sets a setting in this message.
	 * 
	 * @param key
	 * 		the key of the setting
	 * @param value
	 * 		the setting itself
	 */
	public void setSetting(String key, Object value) {
		settings.put(key, value);
	}
	
	/**
	 * @param key
	 * 		the key of the setting.
	 * 
	 * @return
	 * 		the setting with the given key.
	 */
	public Object getSetting(String key) {
		return settings.get(key);
	}
}
