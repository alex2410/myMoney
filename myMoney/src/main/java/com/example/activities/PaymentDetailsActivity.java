package com.example.activities;

import com.example.R;
import com.example.entities.Payment;
import com.example.utils.Constants;
import com.example.utils.helpers.SpendingHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PaymentDetailsActivity extends BaseActivity {

	private SpendingHelper spendingHelper;
	
	//TODO payment details(add/remove) descr, count , name, date, type, details just edited
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spendingHelper = new SpendingHelper(this);
		
		setContentView(R.layout.activity_payment_details);
		Intent intent = getIntent();
		String stringExtra = intent.getStringExtra(Constants.PAYMENT_ID_PARAM);
		if(stringExtra != null && !stringExtra.isEmpty()){
			initPayment(stringExtra);
		}
	}

	private void initPayment(String stringExtra) {
		Payment payment = spendingHelper.getPaymentById(Long.valueOf(stringExtra));
		if(payment != null){
			setTextToField(R.id.lblPayment, "id:"+payment.getId() + " name:"+ payment.getName());
		} else {
			setTextToField(R.id.lblPayment, "error "+stringExtra);
		}
	}

	@Override
	protected Class<? extends Activity> getPrevious() {
		return SpendingTableActivity.class;
	}

}
