package com.example.db.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.R;
import com.example.db.MySQLiteHelper;
import com.example.entities.User;
import com.example.exceptions.AccountException;
import com.example.utils.PasswordHash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

public class UserDAO extends BaseDao implements IUserDao {
	//TODO change pass
	private static final String[] USER_COLS_HASH = new String []{"NAME","CREATE_DATE","ID","IS_ADMIN","HASH"};
	private static final String[] USER_COLS = new String []{"NAME","CREATE_DATE","ID","IS_ADMIN"};
	
	public UserDAO(Context context){
		super(context);
	}
	
	@Override
	public User login(String name, String pass) throws DAOException, AccountException {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getDatabase()
					.query(MySQLiteHelper.TABLE_USERS,USER_COLS_HASH, "NAME=? AND IS_DELETED=?",
							new String[]{name,"false"}, null, null, null);
			cursor.moveToFirst();
			int count = cursor.getCount();
			if(count == 0){
				throw new AccountException(context.getString(R.string.err_user_removed));
			}
			String string = cursor.getString(4);
			if(string == null) {
				return null;
			}
			boolean validatePassword = PasswordHash.validatePassword(pass, string);
			if(!validatePassword){
				throw new AccountException(context.getString(R.string.err_invalid_password));
			}
			User user = User.adaptUser(cursor);
			return user.getId() != null ? user : null;
		} catch (AccountException e){
			throw e;
		} catch (Exception e){
			throw new DAOException(e);
		} finally {
			closeCursor(cursor);
		}
	}

	@Override
	public boolean isUserExists(String name) throws DAOException {
		try {
			return DatabaseUtils.longForQuery(dbHelper.getDatabase(),
					"select count(*) from " + MySQLiteHelper.TABLE_USERS + " where NAME=? limit 1",
					new String[] {name}) > 0;
		} catch (Exception e){
			throw new DAOException(e);
		}
	}

	@Override
	public boolean create(String name, String pass) throws DAOException {
		Cursor cursor = null;
		try {
			ContentValues values = new ContentValues();
			values.put("NAME", name);
			values.put("CREATE_DATE", persistDate(new Date()));
			String generateHash = generateHash(pass);
			if(generateHash == null){
				return false;
			}
			values.put("HASH", generateHash);
			long insertId = dbHelper.getDatabase()
					.insert(MySQLiteHelper.TABLE_USERS, null,values);
			cursor = dbHelper.getDatabase()
					.query(MySQLiteHelper.TABLE_USERS,USER_COLS, "ID" + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			return true;
		} catch (Exception e){
			throw new DAOException(e);
		} finally {
			closeCursor(cursor);
		}
	}

	private String generateHash(String pass) {
		try {
			return PasswordHash.createHash(pass);
		} catch (NoSuchAlgorithmException e) {
			Log.e(getClass().getName(), "Fail generateHash", e);
		} catch (InvalidKeySpecException e) {
			Log.e(getClass().getName(), "Fail generateHash", e);
		}
		return null;
	}

	@Override
	public boolean delete(User user, boolean permanently) throws DAOException {
		try {
			boolean deleted = false;
			if(permanently){//FIXME remove all payments
				deleted = dbHelper.getDatabase().delete(MySQLiteHelper.TABLE_USERS, "ID ="+user.getId(),null) > 0;
			} else {
				ContentValues values = new ContentValues();
				values.put("IS_DELETED", "true");
				int updated = dbHelper.getDatabase().update(MySQLiteHelper.TABLE_USERS, values, "ID="+user.getId(), null);
				deleted = updated >0;
			}
			return deleted;
		} catch (Exception e){
			throw new DAOException(e);
		} 
	}

	@Override
	public boolean isUserAdmin(User user) throws DAOException {
		return user != null && user.isAdmin();
	}

	@Override
	public List<User> getAllUsers() throws DAOException {//FIXME validate
		Cursor cursor = null;
		try {
			List<User> users = new ArrayList<User>();
			cursor = dbHelper.getDatabase()
					.query(MySQLiteHelper.TABLE_USERS,USER_COLS, null, null, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				User u = User.adaptUser(cursor);
				users.add(u);
				cursor.moveToNext();
			}
			return users;
		} catch (Exception e){
			throw new DAOException(e);
		} finally {
			closeCursor(cursor);
		}
	}

	@Override
	public User restoreUser(String name) throws DAOException, AccountException {
		if(isUserExists(name)){
			Cursor cursor = null;
			try {
				cursor = dbHelper.getDatabase()
						.query(MySQLiteHelper.TABLE_USERS,USER_COLS, "NAME=? AND IS_DELETED=?",
								new String[]{name,"false"}, null, null, null);
				cursor.moveToFirst();
				int count = cursor.getCount();
				if(count == 0){
					throw new AccountException(context.getString(R.string.err_user_removed));
				}
				User user = User.adaptUser(cursor);
				return user.getId() != null ? user : null;
			} catch (AccountException e){
				throw e;
			} catch (Exception e){
				throw new DAOException(e);
			} finally {
				closeCursor(cursor);
			}
		}
		return null;
	}

}
