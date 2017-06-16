package com.example.fragments;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.R;
import com.example.activities.BaseActivity;
import com.example.activities.PaymentDetailsActivity;
import com.example.activities.SpendingTableActivity;
import com.example.calculators.TargetCalculator;
import com.example.context.ConstantsContext;
import com.example.entities.Payment;
import com.example.entities.Payment.PaymentType;
import com.example.entities.Period;
import com.example.exceptions.ServiceException;
import com.example.fragments.DatePickerFragment.DatePickedAction;
import com.example.utils.Constants;
import com.example.utils.CurrencyUtils;
import com.example.utils.DateUtils;
import com.example.utils.helpers.ComponentsHelper;
import com.example.utils.helpers.SpendingHelper;
import com.example.utils.helpers.UserHelper;
import com.example.utils.helpers.UserPreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SpendingTableFragment extends BaseFragment {

	public static final String SPENDING_TABLE_FRAGMENT_IS_FIRST_CREATE = "SpendingTableFragmentIsFirstCreate";
	private Period tablePeriod;
	private TextView summLbl;
	private BigDecimal currentSum;
	private TextView targetLbl;
	private UserPreferenceHelper preferenceHelper;
	private SpendingHelper spendingHelper;
	
	public SpendingTableFragment() {
		super(R.layout.fragment_spending_table);
	}
	
	private void initPeriod() {
		String stringFromPref = preferenceHelper.getString(Constants.SEARCH_FROM);
		String stringFromPref2 = preferenceHelper.getString(Constants.SEARCH_TO); 
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Boolean value = ConstantsContext.getValue(SPENDING_TABLE_FRAGMENT_IS_FIRST_CREATE, false);
		if(!value && preferenceHelper.getBoolean(Constants.FIRST_START_CLEAR_PERIOD,true)){
			tablePeriod = new Period(c.getTime(),date);
			saveLastPeriod(tablePeriod);
		} else {
			Date to = stringFromPref2.isEmpty() ? date 
					: (stringFromPref2.equals("all") ? null : DateUtils.parseDate(stringFromPref2));
			tablePeriod = new Period(stringFromPref.isEmpty() ?  c.getTime()
					: (stringFromPref.equals("all") ? null : DateUtils.parseDate(stringFromPref)),to);
		}
	}
	
	@Override
	public final void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		preferenceHelper = new UserPreferenceHelper(getActivity(),new UserHelper(getActivity()));
		spendingHelper = new SpendingHelper(getActivity());
		Button from = (Button) currentView.findViewById(R.id.tblDateFrom);
		ComponentsHelper.setButtonDefaults(from);
		from.setOnClickListener(getClickListener(true));
		Button to = (Button) currentView.findViewById(R.id.tblDateTo);
		ComponentsHelper.setButtonDefaults(to);
		to.setOnClickListener(getClickListener(false));
		Button add = (Button) currentView.findViewById(R.id.addNew);
		ComponentsHelper.setButtonDefaults(add);
		add.setOnClickListener(getAddNewActionListener());
		createSumm();
		initPeriod();
		refreshTable();
		ConstantsContext.put(SPENDING_TABLE_FRAGMENT_IS_FIRST_CREATE, true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initPeriod();
	}
	
	//TODO add rules via prefs , refactoring
	public void refreshTable(){
		Button from = (Button) currentView.findViewById(R.id.tblDateFrom);
		Button to = (Button) currentView.findViewById(R.id.tblDateTo);
		String formatDate = tablePeriod.getFrom() == null ? "" : DateUtils.formatDate(tablePeriod.getFrom(),"");
		from.setText(formatDate.isEmpty() ? "from: <any> " : "from: " + formatDate+" ");
		formatDate = tablePeriod.getTo() == null ? "" : DateUtils.formatDate(tablePeriod.getTo(),"");
		to.setText(formatDate.isEmpty() ? " to: <any> " : " to: " + formatDate+" ");
	
		OnClickListener bachHoverClick = getBachHoverClick();
		TableLayout tbl = (TableLayout) currentView.findViewById(R.id.tableLayout1);
		tbl.setColumnStretchable(0, true);
		tbl.setColumnStretchable(1, true);
		tbl.setColumnStretchable(2, true);
		tbl.setColumnShrinkable(3, true);
		tbl.setColumnShrinkable(4, true);
		tbl.removeAllViews();
		List<Payment> payments = getCurrentPayments();
		BigDecimal sum = new BigDecimal(0);
	
		for(Payment payment : payments){
			TableRow row = new TableRow(currentView.getContext());
			row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
			TextView name = new TextView(currentView.getContext());
			name.setText(payment.getName());
			name.setSingleLine(true);
			name.setEllipsize(TruncateAt.MARQUEE);
			name.setTextSize(14);
			name.setPadding(5, 1, 3, 1);
			row.addView(name);
			
			TextView count = new TextView(currentView.getContext());
			count.setText(payment.getValueCurrency());
			if(payment.getType().equals(PaymentType.SPENT)) {
				sum = sum.add(payment.getValue());
			}
			count.setTextSize(14);
			count.setSingleLine(true);
			count.setEllipsize(TruncateAt.MARQUEE);
			count.setPadding(5, 1, 3, 1);
			count.setTextColor(payment.getType().equals(PaymentType.SPENT) ? Color.RED : Color.GREEN);
			row.addView(count);
			
			TextView when = new TextView(currentView.getContext());
			when.setText(DateUtils.formatDate(payment.getCreateDate()));
			when.setTextSize(14);
			when.setSingleLine(true);
			when.setEllipsize(TruncateAt.MARQUEE);
			when.setPadding(5, 1, 3, 1);
			row.addView(when);

			Button view = new Button(currentView.getContext());
			view.setText("Open");
			ComponentsHelper.setButtonDefaults(view);
			view.setOnClickListener(getViewRowAction(payment));
			row.addView(view);
			
			if(preferenceHelper.getBoolean(Constants.DELETE_PAYMENTS)){
				Button dell = new Button(currentView.getContext());
				dell.setText("X");
				ComponentsHelper.setButtonDefaults(dell);
				dell.setOnClickListener(getDellRowAction(payment,row));
				row.addView(dell);
			}
			
			row.setOnClickListener(bachHoverClick);
			tbl.addView(row);
			
			name.setWidth(name.getWidth()-25);
			when.setWidth(when.getWidth()-25);
			name.setMaxWidth(name.getWidth());
			count.setMaxWidth(name.getWidth());
			when.setMaxWidth(name.getWidth());
		}
		tbl.requestFocus();
		scrollView();
		currentSum = sum;
		summLbl.setText(CurrencyUtils.format(sum,"p "));
		refreshTargetLbl();
		hideKeyboard();
	}

	private void scrollView() {
		final ScrollView view = getComponentById(R.id.scrollForTable);
		view.post(new Runnable() {

	        @Override
	        public void run() {
	        	view.fullScroll(ScrollView.FOCUS_DOWN);
	        }
	    });
	}

	private void refreshTargetLbl() {
		String string = preferenceHelper.getString(Constants.TARGET_SUM_DISPLAY_TYPE);
		String targetSumm = preferenceHelper.getString(Constants.TARGET_SUMM);
		BigDecimal target = null;
		try {
			target = new BigDecimal(targetSumm);
		} catch (Exception e){
			Log.e(getClass().getName(), "Fail target value", e);
		}
		BigDecimal calculate = TargetCalculator.calculate(string,currentSum,target);
		targetLbl.setText(calculate == null ? "" : "Target: " + CurrencyUtils.format(calculate,"p "));
		if(calculate != null){
			targetLbl.setTextColor(calculate.compareTo(BigDecimal.ZERO) > 0 ? Color.GREEN : Color.RED);
		}

	}

	private List<Payment> getCurrentPayments() {
		return spendingHelper.getPaymentsForPeriod(tablePeriod);
	}

	private OnClickListener getBachHoverClick() {
		return new OnClickListener() {
			
			private View old;
			private int drawingCacheBackgroundColor;

			@Override
			public void onClick(View v) {
				if(old != null){
					old.setBackgroundColor(drawingCacheBackgroundColor);
				}
				drawingCacheBackgroundColor = v.getDrawingCacheBackgroundColor();
				old = v;
				v.setBackgroundColor(Color.LTGRAY);
			}
		};
	}
//TODO
	private void createSumm() {
		TableLayout tbl = (TableLayout) currentView.findViewById(R.id.SummRow);
		TableRow header = new TableRow(currentView.getContext());
		header.setPadding(5, 2, 5, 2);
		header.setBackgroundColor(Color.GRAY);
		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
		header.setLayoutParams(lp);
		String[] headers = new String[]{getString(R.string.summ)+":   ","Value", " "};
		tbl.setColumnStretchable(2, true);
		for(int i=0;i<headers.length;i++){
			TextView name = new TextView(currentView.getContext());
			name.setText(headers[i]);
			if(i == 2){
				targetLbl = name;
				name.setGravity(Gravity.CENTER);
			} else if(i == 1){
				summLbl = name;
				name.setGravity(Gravity.RIGHT);
			} else {
				name.setGravity(Gravity.CENTER);
			}
			name.setTextColor(Color.WHITE);
			header.addView(name);
		}
		tbl.addView(header);
	}
	
	private OnClickListener getAddNewActionListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((SpendingTableActivity)getActivity()).getPager().setCurrentItem(1);
			}
		};
	}

	private OnClickListener getClickListener(final boolean isFrom) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerFragment newFragment = new DatePickerFragment(isFrom ? tablePeriod.getFrom() : tablePeriod.getTo());
				newFragment.setAction(getDatePickedAction(isFrom));
				newFragment.show(getFragmentManager(), isFrom ? "From date" : "To date");
			}
		};
	}

	private DatePickedAction getDatePickedAction(final boolean isFrom) {
		return new DatePickedAction() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				dateSetted(DateUtils.getDateFromDatePicket(view));
			}

			private void dateSetted(Date d) {
				Date from = isFrom ?  d: tablePeriod.getFrom() ;
				Date to = isFrom ? tablePeriod.getTo() : d;
				tablePeriod = new Period(from, to);
				saveLastPeriod(tablePeriod);
				refreshTable();
			}

			@Override
			public void onDateSet() {
				dateSetted(null);
			}
		};
	}

	private void saveLastPeriod(Period tablePeriod) {
		preferenceHelper.setString(Constants.SEARCH_FROM,
				tablePeriod.getFrom() == null ? "all" : DateUtils.formatDate(tablePeriod.getFrom()));
		preferenceHelper.setString(Constants.SEARCH_TO,
				tablePeriod.getTo() == null ? "all" : DateUtils.formatDate(tablePeriod.getTo()));
	}

	private OnClickListener getDellRowAction(final Payment payment,final TableRow row) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());                      
			    dlgAlert.setTitle(getString(R.string.warning)); 
			    dlgAlert.setPositiveButton(getString(R.string.yes), getRemoveRowAction(payment,row));
			    dlgAlert.setNegativeButton(getString(R.string.no), null);
			    dlgAlert.setMessage(getString(R.string.remove_row) + payment.getName()+"?"); 
			    dlgAlert.setCancelable(true);
			    dlgAlert.create().show();
			}

			private android.content.DialogInterface.OnClickListener getRemoveRowAction(
					final Payment payment, final TableRow row) {
				return new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(spendingHelper.removePayment(payment)){
							TableLayout tbl = (TableLayout) currentView.findViewById(R.id.tableLayout1);
							tbl.removeView(row);
							substructFromSumm(payment);
							refreshTargetLbl();
						}
					}

					private void substructFromSumm(Payment payment) {
						try {
							if(payment.getType() != PaymentType.GET) {
								currentSum = currentSum.subtract(payment.getValue());
							}
							summLbl.setText(CurrencyUtils.format(currentSum,"p"));
						} catch (Exception e) {
							ServiceException.showMessace(new ServiceException(e), getActivity());
						}
					}
				};
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private OnClickListener getViewRowAction(final Payment payment) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SimpleEntry<String, String> entry = new SimpleEntry<String, String>(Constants.PAYMENT_ID_PARAM,payment.getId()+"");
				((BaseActivity)getActivity()).switchToActivity(PaymentDetailsActivity.class,entry);
			}
		};
	}
}
