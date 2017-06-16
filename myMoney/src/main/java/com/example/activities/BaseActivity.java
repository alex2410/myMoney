package com.example.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

import com.example.R;
import com.example.context.ActivityContext;
import com.example.entities.User;
import com.example.exceptions.AccountException;
import com.example.exceptions.ServiceException;
import com.example.utils.Constants;
import com.example.utils.DateUtils;
import com.example.utils.helpers.MenuHelper;
import com.example.utils.helpers.UserHelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {

    protected MenuHelper menuHelper;
    protected UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuHelper = new MenuHelper(this);
        userHelper = new UserHelper(this);
        setContentView(R.layout.activity_blank);
        User currentUser = userHelper.getCurrentUser();
        if (currentUser == null) {
            if (!trySession()) {
                if (!(getClass() == LoginActivity.class || getClass() == CreateAccountActivity.class)) {
                    switchToActivity(LoginActivity.class);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuHelper.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean b = menuHelper.onOptionsItemSelected(item);
        return b == null ? super.onOptionsItemSelected(item) : b;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Boolean b = menuHelper.onPrepareOptionsMenu(menu);
        return b == null ? super.onPrepareOptionsMenu(menu) : b;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getClass() == LoginActivity.class || getClass() == CreateAccountActivity.class) {
            User currentUser = userHelper.getCurrentUser();
            if (currentUser != null) {
                switchToActivity(SpendingTableActivity.class);
                return;
            }
        }
        Class<? extends Activity> lastActivity = ActivityContext.getContext().getLastActivity();
        if (lastActivity != null && getClass() != lastActivity) {
            switchToActivity(lastActivity);
        }
    }

    @Override
    public void onBackPressed() {
        Class<? extends Activity> previous = getPrevious();
        if (previous != null) {
            switchToActivity(previous);
        }
        super.onBackPressed();
    }

    protected void showError(String text, int idComponent) {
        TextView status = (TextView) findViewById(idComponent);
        if (status == null) {
            return;
        }
        status.setVisibility(TextView.VISIBLE);
        status.setText(text);
        status.setTextColor(Color.RED);
    }

    protected String getTextFromField(int id) {
        TextView text = getComponentById(id);
        return text == null ? "" : text.getText().toString();
    }

    protected boolean setTextToField(int id, String value) {
        TextView text = getComponentById(id);
        if (text != null) {
            text.setText(value);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getComponentById(int id) {
        return (T) findViewById(id);
    }

    public void saveStringToPref(String key, String value) {
        SharedPreferences settings = getSharedPreferences(Constants.PREF_STORAGE, 0);
        Editor edit = settings.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String getStringFromPref(String key) {
        SharedPreferences settings = getSharedPreferences(Constants.PREF_STORAGE, 0);
        return settings.getString(key, "");
    }

    public Integer getIntFromPref(String key) {
        SharedPreferences settings = getSharedPreferences(Constants.PREF_STORAGE, 0);
        return settings.getInt(key, 5 * 60);
    }

    protected Class<? extends Activity> getPrevious() {
        return SpendingTableActivity.class;
    }

    public void switchToActivity(Class<? extends Activity> class1, Entry<String, String>... params) {
        ActivityContext.switchToActivity(this, class1, params);
    }

    public void switchToActivity(Class<? extends Activity> class1) {
        ActivityContext.switchToActivity(this, class1);
    }

    public void saveSession(String textFromField) {
        saveStringToPref(Constants.LOGINED_USER_PREF, textFromField);
        SimpleDateFormat dateFormat = DateUtils.getDateFormat("dd.MMM.yyyy HH:mm:SS");
        saveStringToPref(Constants.LOGINED_USER_LAST_TIME, dateFormat.format(new Date()));
    }

    private boolean trySession() {
        String stringFromPref = getStringFromPref(Constants.LOGINED_USER_PREF);
        if (!stringFromPref.isEmpty()) {
            String ds = getStringFromPref(Constants.LOGINED_USER_LAST_TIME);
            SimpleDateFormat dateFormat = DateUtils.getDateFormat("dd.MMM.yyyy HH:mm:SS");
            Date parseDate = null;
            try {
                parseDate = dateFormat.parse(ds);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            if (parseDate != null) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(parseDate);
                int sessionTimeout = getIntFromPref(Constants.USER_SETTINGS_PREFIX  + Constants.SESSION_TIMEOUT);
                instance.add(Calendar.MINUTE, sessionTimeout);
                boolean valid = instance.getTime().after(new Date());
                boolean a = false;
                if (valid) {
                    try {
                        a = userHelper.restoreUser(stringFromPref);
                    } catch (AccountException e) {
                        ServiceException.showMessace(e, this);
                    }
                }
                return valid && a;
            }
        }
        return false;
    }
}
