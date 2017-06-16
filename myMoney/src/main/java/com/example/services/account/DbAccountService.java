package com.example.services.account;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.example.MyMoneyApp;
import com.example.db.dao.DAOException;
import com.example.db.dao.IUserDao;
import com.example.db.dao.UserDAO;
import com.example.entities.User;
import com.example.exceptions.AccountException;
import com.example.exceptions.ServiceException;

public class DbAccountService implements IAccountService {

    private AtomicReference<User> currentUser;
    private IUserDao userDAO;

    public DbAccountService() {
        currentUser = new AtomicReference<User>();
        userDAO = new UserDAO(MyMoneyApp.getAppContext());
    }

    @Override
    public User getCurrentUser() {
        return currentUser.get();
    }

    @Override
    public User login(String name, String pass) throws ServiceException {
        if (currentUser.get() != null) {
            throw new AccountException("Уже залогинен " + currentUser.get());
        }
        if (!validateIsUserExists(name)) {
            throw new AccountException("Пользователь " + name + " не найден");
        }
        User user = null;
        try {
            user = userDAO.login(name, pass);
        } catch (AccountException e) {
            throw e;
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        boolean compareAndSet = currentUser.compareAndSet(null, user);
        if (!compareAndSet) {
            throw new AccountException("Ошибка пользователя");
        }
        return user;
    }

    @Override
    public boolean validateIsUserExists(String name) throws ServiceException {
        try {
            return userDAO.isUserExists(name);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean createAccount(String name, String pass) throws ServiceException {
        if (validateIsUserExists(name)) {
            throw new AccountException("Пользователь " + name + " уже существует");
        }
        try {
            return userDAO.create(name, pass);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean logout() throws ServiceException {
        currentUser.set(null);
        return true;
    }

    @Override
    public boolean removeUser(User user) throws ServiceException {
        boolean b = false;
        try {
            b = userDAO.delete(user, false);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        boolean compareAndSet = currentUser.compareAndSet(user, null);
        if (!(compareAndSet && b)) {
            throw new AccountException("Не удалось удалить пользователя");
        }
        return true;
    }

    @Override
    public boolean removeUserPermanently(User user) throws ServiceException {
        boolean b = false;
        try {
            b = userDAO.delete(user, true);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        boolean compareAndSet = currentUser.compareAndSet(user, null);
        if (!(compareAndSet && b)) {
            throw new AccountException("Не удалось удалить пользователя");
        }
        return true;
    }

    @Override
    public boolean isUserAdmin(User user) throws ServiceException {
        try {
            return userDAO.isUserAdmin(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDAO.getAllUsers();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean restoreUser(String name) throws ServiceException {
        try {
            User user = userDAO.restoreUser(name);
            currentUser.set(user);
            return user != null;
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

}
