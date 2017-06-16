package com.example.activities;

import java.util.List;

import com.example.R;
import com.example.context.ActivityContext;
import com.example.entities.User;
import com.example.utils.Constants;
import com.example.utils.helpers.MenuHelper;
import com.example.utils.helpers.UserHelper;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {

	private MenuHelper menuHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuHelper = new MenuHelper(this);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuHelper.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Boolean b = menuHelper.onOptionsItemSelected(item);
		return b == null ? super.onOptionsItemSelected(item) : b;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Boolean b = menuHelper.onPrepareOptionsMenu(menu);
		return b == null ? super.onPrepareOptionsMenu(menu) : b;
	}
	
	@Override
	public void onBackPressed() {
		ActivityContext.switchToActivity(this, SpendingTableActivity.class);
		//super.onBackPressed();
	}
	
	public static class BasePrefFragment extends PreferenceFragment {
		
		protected User currentUser;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			currentUser = new UserHelper(getActivity()).getCurrentUser();
			
			PreferenceManager prefMgr = getPreferenceManager();
			prefMgr.setSharedPreferencesName(Constants.USER_SETTINGS_PREFIX + currentUser.getName());
			prefMgr.setSharedPreferencesMode(PreferenceActivity.MODE_PRIVATE);
		}
		
	}

	public static class Prefs1Fragment extends BasePrefFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.main_preferences);
		}
	}
	
	public static class Prefs2Fragment extends BasePrefFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.calculations_preferences);
		}
	}
}
