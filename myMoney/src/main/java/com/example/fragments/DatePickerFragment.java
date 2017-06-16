package com.example.fragments;

import java.util.Calendar;
import java.util.Date;

import com.example.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private DatePickedAction action;
	private Date date;
	private boolean showBtnNeutral;
	
	public DatePickerFragment(){
		this(new Date());
	}
	
	public DatePickerFragment(Date date) {
		this(date,true);
	}

	public DatePickerFragment(Date date, boolean showBtnNeutral) {
		this.date = date;
		this.showBtnNeutral = showBtnNeutral;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date == null ? new Date() : date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
		if(showBtnNeutral) {
			datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, getString(R.string.havent_value), getListener());
		}
		return datePickerDialog;
	}
	
	private OnClickListener getListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(action != null) {
					action.onDateSet();
				}
			}
		};
	}

	public void setAction(DatePickedAction action) {
		this.action = action;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		if(action != null) {
			action.onDateSet(view, year, month, day);
		}
	}
	
	public abstract static class DatePickedAction{
		public abstract void onDateSet(DatePicker view, int year, int month, int day);
		public abstract void onDateSet();
	}
}