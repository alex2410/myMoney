package com.example.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.R;
import com.example.entities.Payment;
import com.example.entities.Period;
import com.example.entities.User;
import com.example.utils.DateUtils;
import com.example.utils.helpers.ComponentsHelper;
import com.example.utils.helpers.ExportHelper;
import com.example.utils.helpers.SpendingHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends BaseActivity {

	private ExportHelper exportHelper;
	private SpendingHelper spendingHelper;
	//TODO show user info, remove not permanently, just setDeleted
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		exportHelper = new ExportHelper(this);
		spendingHelper = new SpendingHelper(this);
		setContentView(R.layout.activity_user);
		findViewById(R.id.btnRemoveUser).setOnClickListener(getRemoveUserDialogAction());
		findViewById(R.id.btnExportData).setOnClickListener(getExportPaymentsAction());
		ComponentsHelper.setButtonDefaults((Button) findViewById(R.id.btnRemoveUser));
		ComponentsHelper.setButtonDefaults((Button) findViewById(R.id.btnExportData));
	}

	@Override
	protected void onStart() {
		super.onStart();
		User currentUser = userHelper.getCurrentUser();
		if(currentUser != null){
			((TextView)getComponentById(R.id.userName)).setText("User name: " + currentUser.getName());
			((TextView)getComponentById(R.id.userId)).setText("Id: " + currentUser.getId());
		} else {
			((TextView)getComponentById(R.id.userName)).setText("user null");
		}
	}
	
	private OnClickListener getRemoveUserDialogAction() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(UserActivity.this);                      
			    dlgAlert.setTitle(getString(R.string.warning)); 
			    dlgAlert.setPositiveButton(getString(R.string.yes), getRemoveUserAction());
			    dlgAlert.setNegativeButton(getString(R.string.no), null);
			    dlgAlert.setMessage(getString(R.string.remove_user)); 
			    dlgAlert.setCancelable(true);
			    dlgAlert.create().show();
			}
		};
	}
	
	private OnClickListener getExportPaymentsAction() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Payment> paymentsForPeriod = spendingHelper.getPaymentsForPeriod(new Period(null, null));
				SimpleDateFormat dateFormat = DateUtils.getDateFormat("dd-MM-yy");
				String name = "exportedPayments_"+dateFormat.format(new Date())+".txt";
				exportHelper.export(paymentsForPeriod,name);
			}
		};
	}


	private android.content.DialogInterface.OnClickListener getRemoveUserAction() {
		return new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				userHelper.removeUser(false);
			}
		};
	}

}
