package com.example.utils.helpers;

import com.example.R;
import com.example.activities.SettingsActivity;
import com.example.context.ActivityContext;
import com.example.context.ConstantsContext;
import com.example.entities.User;
import com.example.fragments.SpendingTableFragment;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MenuHelper {
	
	private Activity activity;
	private UserHelper userHelper;

	public MenuHelper(Activity activity){
		this.activity = activity;
		userHelper = new UserHelper(activity);
	}

	public void onCreateOptionsMenu(Menu menu) {
		activity.getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		activity.getMenuInflater().inflate(R.menu.go_admin_menu, menu);
		activity.getMenuInflater().inflate(R.menu.logout, menu);
	}

	public Boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.logout_action) {
			userHelper.logOutAction();
			ConstantsContext.put(SpendingTableFragment.SPENDING_TABLE_FRAGMENT_IS_FIRST_CREATE, false);
			return true;
		}
		if (id == R.id.current_user) {
			userHelper.showUser();
			return true;
		}
		if (id == R.id.go_admin_tools_action) {
			userHelper.showAdminTools();
			return true;
		}
		if (id == R.id.action_settings) {
			ActivityContext.switchToActivity(activity,SettingsActivity.class);
			return true;
		}
		return null;
	}

	public Boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem settings = menu.findItem(R.id.action_settings);
		MenuItem logout= menu.findItem(R.id.logout_action);
		MenuItem adminMenu= menu.findItem(R.id.go_admin_tools_action);
		MenuItem currUser = menu.findItem(R.id.current_user);
		User currentUser = userHelper.getCurrentUser();
		settings.setVisible(currentUser != null);
		if(currUser != null) {
			currUser.setTitleCondensed(currentUser == null ? "" : currentUser.getName());
		}
		if(logout != null) {
			logout.setVisible(currentUser != null);
		}
		if(adminMenu != null) {
			adminMenu.setVisible(userHelper.isUserAdmin());
		}
		return null;
	}
	
}
