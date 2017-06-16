package com.example.context;

import java.util.HashMap;
import java.util.Map;

public final class ConstantsContext {
	
	private static final Map<String, Object> VALS;
	
	static {
		VALS = new HashMap<String, Object>();
	}
	
	private ConstantsContext() {
		
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(String key, T defaultVal) {
		try {
			return VALS.get(key) == null ? defaultVal : (T) VALS.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultVal;
	}

	public static void put(String key, Object obj) {
		VALS.put(key, obj);
	}
}
