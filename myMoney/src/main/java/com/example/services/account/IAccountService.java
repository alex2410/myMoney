package com.example.services.account;

import java.util.List;

import com.example.entities.User;
import com.example.exceptions.ServiceException;
import com.example.services.IService;

public interface IAccountService extends IService{

	boolean logout() throws ServiceException;
	User login(String name, String pass) throws ServiceException;
	boolean validateIsUserExists(String name) throws ServiceException;
	boolean createAccount(String name, String pass) throws ServiceException;
	User getCurrentUser();
	boolean removeUser(User user) throws ServiceException;
	boolean removeUserPermanently(User user) throws ServiceException;
	boolean isUserAdmin(User user) throws ServiceException;
	List<User> getAllUsers() throws ServiceException;
	boolean restoreUser(String name) throws ServiceException;
}
