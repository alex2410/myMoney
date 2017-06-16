package com.example.exceptions;

import com.example.R;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

public class ServiceException extends Exception{

	public ServiceException(String text) {
		super(text);
	}

	public ServiceException(Throwable thr) {
		super(thr);
	}
//TODO refactoring
	public static void showMessace(ServiceException e, Context ctx) {
		if(e.getCause() instanceof Exception) {
			Log.e(ctx.getClass().getName(), "Fail", e);
		}
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ctx);                      
	    dlgAlert.setTitle(ctx.getString(R.string.error)); 
	    dlgAlert.setPositiveButton(ctx.getString(R.string.ok), null);
	    dlgAlert.setMessage(e.getMessage()); 
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
	}
	
	public static void showMessace(String text, Context ctx) {
		Log.i(ctx.getClass().getName(), "Info: "+ text);
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ctx);                      
	    dlgAlert.setTitle(ctx.getString(R.string.info)); 
	    dlgAlert.setPositiveButton(ctx.getString(R.string.ok), null);
	    dlgAlert.setMessage(text); 
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
	}
}
