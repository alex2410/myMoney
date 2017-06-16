package com.example.db.dao;

import java.util.Date;

import com.example.db.MySQLiteHelper;

import android.content.Context;
import android.database.Cursor;

public class BaseDao {

	protected MySQLiteHelper dbHelper;
	protected Context context;
	
	public BaseDao(Context context) {
		this.context = context;
		dbHelper = new MySQLiteHelper(context);
	}
	
	public static Long persistDate(Date date) {
	    if (date != null) {
	        return date.getTime();
	    }
	    return null;
	}
	
	public static Date loadDate(Long millis) {
	    return new Date(millis);
	}

	public void closeCursor(Cursor cursor) {
		if(cursor != null) {
			cursor.close();
		}
	}
}
