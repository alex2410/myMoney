package com.example.db.dao;

import java.util.List;

import com.example.entities.User;
import com.example.exceptions.AccountException;

public interface IUserDao {

	User login(String name, String pass) throws DAOException, AccountException;
	boolean isUserExists(String name) throws DAOException;
	boolean create(String name, String pass) throws DAOException;
	boolean delete(User user, boolean permanently) throws DAOException;
	boolean isUserAdmin(User user) throws DAOException;
	List<User> getAllUsers() throws DAOException;
	User restoreUser(String name) throws DAOException, AccountException;

}
