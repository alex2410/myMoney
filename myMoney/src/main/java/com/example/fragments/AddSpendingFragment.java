package com.example.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.R;
import com.example.activities.SpendingTableActivity;
import com.example.entities.Payment;
import com.example.entities.Payment.PaymentType;
import com.example.entities.Period;
import com.example.fragments.DatePickerFragment.DatePickedAction;
import com.example.utils.DateUtils;
import com.example.utils.helpers.ComponentsHelper;
import com.example.utils.helpers.SpendingHelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class AddSpendingFragment extends BaseFragment {

	private Date addingDate;
	private SpendingHelper spendingHelper;
	
	public AddSpendingFragment() {
		super(R.layout.fragment_add_spending);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		spendingHelper = new SpendingHelper(getActivity());
		addingDate = new Date();
		
		Button date = (Button) currentView.findViewById(R.id.buttonDateAdding);
		ComponentsHelper.setButtonDefaults(date);
		date.setOnClickListener(getClickListener());
		date.setText(DateUtils.formatDate(addingDate));
		
		Button add = (Button) currentView.findViewById(R.id.buttonAddSpending);
		ComponentsHelper.setButtonDefaults(add);
		add.setOnClickListener(getAddClickListener());
		
		AutoCompleteTextView componentById = getComponentById(R.id.spentNameAuto);
		componentById.setAdapter(getSpendingAdapter());
		componentById.setThreshold(1);
		
		currentView.findViewById(R.id.status).setVisibility(TextView.INVISIBLE);
		
		Spinner sp = getComponentById(R.id.spinner1);
		List<PaymentType> spinnerArray =  new ArrayList<PaymentType>();
		spinnerArray.add(PaymentType.SPENT);
		spinnerArray.add(PaymentType.GET);
		ArrayAdapter<PaymentType> adapter = new ArrayAdapter<PaymentType>(getActivity()
		    , android.R.layout.simple_spinner_item, spinnerArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
	}
	
	private OnClickListener getAddClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!validateInput()){
					return;
				}
				Payment p = getCurrentPayment();
				spendingHelper.addSpending(p);
				clearFields();
				((SpendingTableActivity)getActivity()).getPager().setCurrentItem(0);
				((SpendingTableActivity)getActivity()).refreshTable();
			}
		};
	}
	
	private void clearFields() {
		((TextView)getComponentById(R.id.spentNameAuto)).setText("");
		((TextView)getComponentById(R.id.textCost)).setText("");
		((TextView)getComponentById(R.id.description)).setText("");
	}

	private Payment getCurrentPayment() {
		Payment p = new Payment();
		p.setName(getTextFromField(R.id.spentNameAuto));
		Spinner sp = getComponentById(R.id.spinner1);
		p.setType((PaymentType) sp.getSelectedItem());
		p.setValue(new BigDecimal(getTextFromField(R.id.textCost)));
		p.setDescription(getTextFromField(R.id.description));
		p.setCreateDate(addingDate);
		return p;
	}

	private boolean validateInput() {
		if(getTextFromField(R.id.spentNameAuto).isEmpty()){
			showError(getString(R.string.err_enter_product),R.id.status);
			return false;
		}
		if (getTextFromField(R.id.textCost).isEmpty()) {
			showError(getString(R.string.err_enter_cost),R.id.status);
			return false;
		}
		try {
			new BigDecimal(getTextFromField(R.id.textCost));
		} catch (Exception e){
			showError(e.getMessage(), R.id.status);
			return false;
		}
		return true;
	}

	private OnClickListener getClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerFragment newFragment = new DatePickerFragment(addingDate,false);
				newFragment.setAction(getDatePickedAction());
				newFragment.show(getFragmentManager(), "Date");
			}
		};
	}

	protected DatePickedAction getDatePickedAction() {
		return new DatePickedAction() {
			
			@Override
			public void onDateSet() {
			}
			
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				addingDate = DateUtils.getDateFromDatePicket(view);
				Button date = (Button) currentView.findViewById(R.id.buttonDateAdding);
				date.setText(DateUtils.formatDate(addingDate));
			}
		};
	}
	
	private ArrayAdapter<String> getSpendingAdapter() {
		Date to = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(to);
		c.add(Calendar.MONTH, -2);
		Date from = c.getTime();
		List<Payment> paymentsForPeriod = spendingHelper.getPaymentsForPeriod(new Period(from, to));
		Set<String> keys = spendingHelper.groupPaymentsByKey(paymentsForPeriod);
		return new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item_1line,keys.toArray(new String[keys.size()]));
	}
}
