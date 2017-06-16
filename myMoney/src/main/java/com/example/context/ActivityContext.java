package com.example.context;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import android.app.Activity;
import android.content.Intent;

public class ActivityContext {
	
	static {
		instance = new ActivityContext();
	}
	
	private static ActivityContext instance;
	private AtomicReference<Class<? extends Activity>> last;

	public ActivityContext(){
		last = new AtomicReference<Class<? extends Activity>>();
	}
	
	
	public void setLastActivity(Class<? extends Activity> cls){
		last.set(cls);
	}
	
	public Class<? extends Activity> getLastActivity(){
		return last.get();
	}

	public static ActivityContext getContext(){
		return instance;
	}

	public void dropHistory() {
		last.set(null);
	}
	
	@SuppressWarnings("unchecked")
	public static void switchToActivity(Activity act,Class<? extends Activity> cls) {
		switchToActivity(act,cls,new SimpleEntry[0]);
	}
	
	public static void switchToActivity(Activity act, Class<? extends Activity> cls, Entry<String, String>... params) {
		Intent myIntent = new Intent(act, cls);
		if(params.length != 0){
			for(Entry<String, String> p : params){
				myIntent.putExtra(p.getKey(), p.getValue());
			}
		}
		act.startActivity(myIntent);
		ActivityContext.getContext().setLastActivity(cls);
		act.finish();
	}
}
