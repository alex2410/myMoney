package com.example.entities;

import com.example.db.dao.BaseDao;

import android.database.Cursor;

public class User extends Entity {

	private String name;
	private boolean isAdmin;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", Id=" + getId()
				+ ", createDate=" + getCreateDate() + "]";
	}

	public static User adaptUser(Cursor cursor) {
		User user = new User();
		user.adapt(cursor);
		return user;
	}
	
	@Override
	public User adapt(Cursor cursor) {
		setName(cursor.getString(0));
		setCreateDate(BaseDao.loadDate(cursor.getLong(1)));
		setId((long) cursor.getInt(2));
		setAdmin(cursor.getString(3).equals("true"));
		return this;
	}
}