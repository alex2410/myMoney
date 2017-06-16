package com.example.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.db.MySQLiteHelper;
import com.example.entities.Payment;
import com.example.entities.Period;
import com.example.entities.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SpendingDAO extends BaseDao implements ISpendingDao {
	//TODO  widget
	//TODO refactoring  entity to ContentValues
	private static final String[] SPENDING_COLS =
			new String []{"NAME","CREATE_DATE","ID","DESCRIPTION","VALUE","TYPE","USER_ID"};
	
	public SpendingDAO(Context context) {
		super(context);
	}
	
	@Override
	public List<Payment> getUserPaymentsForPeriod(User user, Period period) throws DAOException {
		List<Payment> pays = new ArrayList<Payment>();
		Cursor cursor = null;
		try {
			String where = "USER_ID=?";
			String[] params = new String[]{user.getId().toString()};
			if(period.getFrom() != null && period.getTo() != null){
				where += " AND CREATE_DATE>=? AND CREATE_DATE<=?";
				params = new String[]{user.getId().toString(),
						persistDate(period.getFrom()).toString(),
						persistDate(period.getTo()).toString()};
			} else if(period.getFrom() != null){
				where += " AND CREATE_DATE>=?";
				params = new String[]{user.getId().toString(),persistDate(period.getFrom()).toString()};
			} else if(period.getTo() != null){
				where += " AND CREATE_DATE<=?";
				params = new String[]{user.getId().toString(),persistDate(period.getTo()).toString()};
			}
			cursor = dbHelper.getDatabase()
					.query(MySQLiteHelper.TABLE_PAYMENTS,SPENDING_COLS, where, params, null, null, "CREATE_DATE ASC");
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				Payment p = Payment.adaptPayment(cursor);
				pays.add(p);
				cursor.moveToNext();
			}
		} catch (Exception e){
			throw new DAOException(e);
		} finally {
			closeCursor(cursor);
		}
		return pays;
	}
	
	@Override
	public boolean removePayment(User user, Payment payment) throws DAOException {
		try {
			boolean deleted = dbHelper.getDatabase().delete(MySQLiteHelper.TABLE_PAYMENTS, "USER_ID=? AND ID=?",
					new String[]{user.getId().toString(),payment.getId().toString()}) > 0;
					return deleted;
		} catch (Exception e){
			throw new DAOException(e);
		}
	}

	@Override
	public Payment getPaymentById(Long id) throws DAOException {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getDatabase()
					.query(MySQLiteHelper.TABLE_PAYMENTS,SPENDING_COLS, "ID" + " = " + id, null, null, null, null);
			cursor.moveToFirst();
			Payment cursorToPayment = Payment.adaptPayment(cursor);
			return cursorToPayment;
		} catch (Exception e){
			throw new DAOException(e);
		} finally {
			closeCursor(cursor);
		}
	}

	@Override
	public boolean addSpending(User user, Payment payment) throws DAOException {
		Cursor cursor = null;
		try {
			ContentValues values = new ContentValues();
			values.put("USER_ID", user.getId());
			values.put("NAME", payment.getName());
			values.put("DESCRIPTION", payment.getDescription());
			values.put("CREATE_DATE", persistDate(payment.getCreateDate()));
			values.put("VALUE", payment.getValue().toString());
			values.put("TYPE", payment.getType().toString()); 
			long insertId = dbHelper.getDatabase()
					.insert(MySQLiteHelper.TABLE_PAYMENTS, null,values);
			cursor = dbHelper.getDatabase()
					.query(MySQLiteHelper.TABLE_PAYMENTS,SPENDING_COLS, "ID" + " = " + insertId, 
							null, null, null, null);
			cursor.moveToFirst();
			boolean created = cursor.getCount() > 0;
			payment.setId(insertId);
			cursor.close();
			return created;
		} catch (Exception e){
			throw new DAOException(e);
		} finally {
			closeCursor(cursor);
		}
	}

}
