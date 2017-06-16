package com.example.activities;

import com.example.R;
import com.example.exceptions.AccountException;
import com.example.utils.Constants;
import com.example.utils.helpers.ComponentsHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class CreateAccountActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		findViewById(R.id.status).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.btnCreate).setOnClickListener(getCreateAction());
		ComponentsHelper.setButtonDefaults((Button) findViewById(R.id.btnCreate));
		findViewById(R.id.fldName).requestFocus();
	}

	private boolean validateInput() {
		if(getTextFromField(R.id.fldName).isEmpty()){
			showError(getString(R.string.err_enter_name),R.id.status);
			return false;
		}
		String textFromField = getTextFromField(R.id.fldPass);
		if(textFromField.isEmpty()){
			showError(getString(R.string.err_enter_password),R.id.status);
			return false;
		}
		String textFromField2 = getTextFromField(R.id.fldPassRe);
		if(textFromField2.isEmpty()){
			showError(getString(R.string.err_enter_password2),R.id.status);
			return false;
		}
		if(!textFromField2.equals(textFromField)){
			showError(getString(R.string.err_password_compare),R.id.status);
			return false;
		}
		return true;
	}
	
	
	private OnClickListener getCreateAction() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!validateInput()){
					return;
				}
				CheckBox box = getComponentById(R.id.chbLoginOnCreate);
				try {
					String name = getTextFromField(R.id.fldName);
					boolean createAccount = userHelper.createAccount(name, getTextFromField(R.id.fldPass));
					if(!createAccount){
						return;
					}
					if(box.isChecked()){
						userHelper.login(name, getTextFromField(R.id.fldPass));
						saveStringToPref(Constants.LAST_USER_PREF, name);
						switchToActivity(SpendingTableActivity.class);
					} else {
						switchToActivity(LoginActivity.class);
					}
				} catch (AccountException e) {
					showError(e.getMessage(),R.id.status);
				}
			}
		};
	}

	@Override
	protected Class<? extends Activity> getPrevious() {
		return LoginActivity.class;
	}
}
