package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_USERS = "Users";
	public static final String TABLE_USERS_CREATE = "CREATE TABLE Users ( ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "CREATE_DATE INTEGER NOT NULL,"
			+ "NAME        STRING  UNIQUE,"
			+ "IS_DELETED  BOOLEAN DEFAULT false,"
			+ "HASH        STRING,"
			+ "IS_ADMIN    BOOLEAN DEFAULT false);";
	
	//USER_ID
	
	public static final String TABLE_PAYMENTS= "Payments";
	public static final String TABLE_PAYMENTS_CREATE = "CREATE TABLE Payments ( ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "CREATE_DATE INTEGER NOT NULL,"
			+ "NAME        STRING,"
			+ "DESCRIPTION        STRING,"
			+ "VALUE        STRING,"
			+ "TYPE        STRING,"
			+ "USER_ID    INTEGER NOT NULL);";

	private static final String DATABASE_NAME = "myMoney.db";
	private static final int DATABASE_VERSION = 7;
	private SQLiteDatabase database;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_USERS_CREATE);
		database.execSQL(TABLE_PAYMENTS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
		onCreate(db);
	}
	
	public SQLiteDatabase getDatabase() {
		return database;
	}

}