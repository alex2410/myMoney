package com.example.activities;

import com.example.R;
import com.example.context.ActivityContext;
import com.example.exceptions.AccountException;
import com.example.utils.Constants;
import com.example.utils.helpers.ComponentsHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById(R.id.status).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.btnLogin).setOnClickListener(getLoginAction());
		findViewById(R.id.btnCreate).setOnClickListener(getCreateAction());
		ComponentsHelper.setButtonDefaults((Button) findViewById(R.id.btnLogin));
		ComponentsHelper.setButtonDefaults((Button) findViewById(R.id.btnCreate));
		findViewById(R.id.fldName).requestFocus();
		setTitle(R.string.title_activity_login);
		ActivityContext.getContext().setLastActivity(LoginActivity.class);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		String stringFromPref = getStringFromPref(Constants.LAST_USER_PREF);
		setTextToField(R.id.fldName, stringFromPref);
	}

	private boolean validateInput() {
		if(getTextFromField(R.id.fldName).isEmpty()){
			showError(getString(R.string.err_enter_name),R.id.status);
			return false;
		}
		if (getTextFromField(R.id.fldPass).isEmpty()) {
			showError(getString(R.string.err_enter_password),R.id.status);
			return false;
		}
		return true;
	}
	
	private OnClickListener getLoginAction() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!validateInput()){
					return;
				}
				try {
					boolean login = userHelper.login(getTextFromField(R.id.fldName), getTextFromField(R.id.fldPass));
					if(login) {
						saveStringToPref(Constants.LAST_USER_PREF, getTextFromField(R.id.fldName));
						saveSession(getTextFromField(R.id.fldName));
						switchToActivity(SpendingTableActivity.class);
					}
				} catch (AccountException e) {
					showError(e.getMessage(),R.id.status);
				}
			}
		};
	}
	
	private OnClickListener getCreateAction() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchToActivity(CreateAccountActivity.class);
			}
		};
	}

	@Override
	protected Class<? extends Activity> getPrevious() {
		return null;
	}
}
