package com.helpermonkey.common;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UserPreferences {
	private static final String BUNDLE_NAME = "UserPreferences";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private UserPreferences() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
