package com.example;

import android.app.Application;
import android.content.Context;

public class MyMoneyApp extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		MyMoneyApp.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return MyMoneyApp.context;
	}

}
