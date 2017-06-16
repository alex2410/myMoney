package com.example.services.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.example.entities.User;
import com.example.exceptions.AccountException;
import com.example.exceptions.ServiceException;

public class SimpleAccountService implements IAccountService{

	private AtomicReference<User> currentUser;
	private Map<String,User> users = new HashMap<String, User>();
	
	public SimpleAccountService(){
		currentUser = new AtomicReference<User>();
		User q = new User();
		q.setId(0L);
		q.setName("q");
		q.setCreateDate(new Date());
		users.put("q", q);
	}
	
	@Override
	public User getCurrentUser() {
		return currentUser.get();
	}
	
	@Override
	public User login(String name, String pass) throws ServiceException{
		if(currentUser.get() != null){
			throw new AccountException("Уже залогинен " + currentUser.get());
		}
		if(!validateIsUserExists(name)){
			throw new AccountException("Пользователь " + name + " не найден");
		}
		User user = users.get(name);
		boolean compareAndSet = currentUser.compareAndSet(null, user);
		if(!compareAndSet){
			throw new AccountException("Ошибка пользователя");
		}
		return user;
	}

	@Override
	public boolean validateIsUserExists(String name)  throws ServiceException{
		return users.containsKey(name);
	}

	@Override
	public boolean createAccount(String name, String pass) throws ServiceException {
		if(validateIsUserExists(name)){
			throw new AccountException("Пользователь " + name + " уже существует");
		}
		User user = new User();
		user.setId((long)users.size());
		user.setName(name);
		user.setCreateDate(new Date());
		users.put(name, user);
		return true;
	}

	@Override
	public boolean logout() throws ServiceException {
		currentUser.set(null);
		return true;
	}

	@Override
	public boolean removeUser(User user) throws ServiceException {
		boolean compareAndSet = currentUser.compareAndSet(user, null);
		if(compareAndSet) {
			users.remove(user.getName());
		} else {
			throw new AccountException("Не удалось удалить пользователя");
		}
		return true;
	}

	@Override
	public boolean isUserAdmin(User user) throws ServiceException {
		return user != null && user.getName().equals("q");
	}

	@Override
	public List<User> getAllUsers() throws ServiceException {
		return new ArrayList<User>(users.values());
	}

	@Override
	public boolean removeUserPermanently(User user) throws ServiceException {
		return removeUser(user);
	}

	@Override
	public boolean restoreUser(String name) throws ServiceException {
		User user = users.get(name);
		currentUser.set(user);
		return true;
	}

}
