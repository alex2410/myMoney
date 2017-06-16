package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;
import android.widget.DatePicker;

public final class DateUtils {

	private DateUtils(){
		
	}

	private static ThreadLocal<SimpleDateFormat> sdfHolder = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("dd.MM.yy",Locale.getDefault());
		};
	};
	public static String formatDate(Date d){
		return formatDate(d," ");
	}
	
	public static String formatDate(Date d,String prefPostfix){
		return d == null ? "" : prefPostfix + sdfHolder.get().format(d) + prefPostfix;
	}
	
	public static Date parseDate(String date){
		try {
			return date == null || date.isEmpty() ? null : sdfHolder.get().parse(date);
		} catch (ParseException e) {
			Log.e(DateUtils.class.getName(), "Fail parseDate\n\r"+date, e);
		}
		return null;
	}
	
	public static SimpleDateFormat getDateFormat(){
		return getDateFormat("dd.MM.yy");
	}
	
	public static SimpleDateFormat getDateFormat(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
		return sdf;
	}
	
	public static java.util.Date getDateFromDatePicket(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}
	
	public static Date getEndOfDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}

	public static Date getStartOfDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
}
