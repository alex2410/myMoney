package com.example.utils.helpers;

import com.example.entities.User;
import com.example.utils.Constants;

import android.app.Activity;
import android.content.SharedPreferences;

public class UserPreferenceHelper {
	
	private Activity activity;
	private UserHelper userHelper;

	public UserPreferenceHelper(Activity activity, UserHelper userHelper){
		this.activity = activity;
		this.userHelper = userHelper;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key,false);
	}

	public boolean getBoolean(String key, boolean defaultVal) {
		SharedPreferences settings = getSharedPref();
		return settings.getBoolean(key, defaultVal);
	}

	public String getString(String key) {
		SharedPreferences settings = getSharedPref();
		return settings.getString(key, "");
	}

	public void setString(String key, String value) {
		SharedPreferences settings = getSharedPref();
		settings.edit().putString(key, value).commit();
	}

	
	public SharedPreferences getSharedPref() {
		SharedPreferences settings = activity.getSharedPreferences(getCurrentPrefName(), 0);
		return settings;
	}
	
	public SharedPreferences getSharedPref(String userName) {
		SharedPreferences settings = activity.getSharedPreferences(Constants.USER_SETTINGS_PREFIX+userName, 0);
		return settings;
	}

	private String getCurrentPrefName() {
		User currentUser = userHelper.getCurrentUser();
		if(currentUser != null){
			return Constants.USER_SETTINGS_PREFIX + currentUser.getName();
		}
		return "";
	}
}
