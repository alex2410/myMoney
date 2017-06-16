package com.example.utils.helpers;

import com.example.activities.AdminToolsActivity;
import com.example.activities.BaseActivity;
import com.example.activities.LoginActivity;
import com.example.activities.UserActivity;
import com.example.context.ActivityContext;
import com.example.context.ServiceContext;
import com.example.entities.User;
import com.example.exceptions.AccountException;
import com.example.exceptions.ServiceException;
import com.example.services.account.IAccountService;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class UserHelper {

    private Activity activity;
    private UserPreferenceHelper preferenceHelper;

    public UserHelper(Activity activity) {
        this.activity = activity;
        preferenceHelper = new UserPreferenceHelper(activity, this);
    }

    public User getCurrentUser() {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            User currentUser = service.getCurrentUser();
            return currentUser;
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail getCurrentUser");
            ServiceException.showMessace(new ServiceException(e), activity);
        }

        return null;
    }

    public void logOutAction() {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            service.logout();
            ActivityContext.getContext().dropHistory();
            ActivityContext.switchToActivity(activity, LoginActivity.class);
            if (activity instanceof BaseActivity) {
                ((BaseActivity) activity).saveSession("");
            }
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail logOutAction");
            ServiceException.showMessace(new ServiceException(e), activity);
        }
    }

    public void showUser() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            ActivityContext.switchToActivity(activity, UserActivity.class);
        }
    }

    public void showAdminTools() {
        if (isUserAdmin()) {
            ActivityContext.switchToActivity(activity, AdminToolsActivity.class);
        }
    }

    public boolean isUserAdmin() {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            User currentUser = getCurrentUser();
            return service.isUserAdmin(currentUser);
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail isUserAdmin");
            ServiceException.showMessace(new ServiceException(e), activity);
        }
        return false;
    }

    public void removeUser(boolean isPermanently) {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            User currentUser = service.getCurrentUser();
            String name = currentUser.getName();
            boolean removeUser = isPermanently ? service.removeUserPermanently(currentUser) : service.removeUser(currentUser);
            if (removeUser && isPermanently) {
                ActivityContext.getContext().dropHistory();
                SharedPreferences sharedPref = preferenceHelper.getSharedPref(name);
                if (sharedPref != null) {
                    boolean commit = sharedPref.edit().clear().commit();
                    if (!commit) {
                        ServiceException.showMessace(new ServiceException("Не удалось удалить настройки"), activity);
                    }
                }
            }
            ActivityContext.switchToActivity(activity, LoginActivity.class);
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail removeUser, isPermanently =" + isPermanently);
            ServiceException.showMessace(new ServiceException(e), activity);
        }
    }

    public boolean createAccount(String name, String pass) throws AccountException {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            return service.createAccount(name, pass);
        } catch (AccountException e) {
            throw e;
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail createAccount, name =" + name + " passEmpty=" + pass.isEmpty());
            ServiceException.showMessace(new ServiceException(e), activity);
        }
        return false;
    }

    public boolean login(String name, String pass) throws AccountException {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            User login = service.login(name, pass);
            if (login != null) {
                return true;
            }
        } catch (AccountException e) {
            throw e;
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail login, name =" + name + " passEmpty=" + pass.isEmpty());
            ServiceException.showMessace(new ServiceException(e), activity);
        }
        return false;
    }

    public boolean restoreUser(String name) throws AccountException {
        try {
            IAccountService service = ServiceContext.getService(IAccountService.class);
            return service.restoreUser(name);
        } catch (AccountException e) {
            throw e;
        } catch (ServiceException e) {
            ServiceException.showMessace(e, activity);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fail restoreUser, name =" + name);
            ServiceException.showMessace(new ServiceException(e), activity);
        }
        return false;
    }
}
