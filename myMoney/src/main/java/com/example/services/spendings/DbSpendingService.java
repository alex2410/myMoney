package com.example.services.spendings;

import java.util.List;

import com.example.MyMoneyApp;
import com.example.db.dao.DAOException;
import com.example.db.dao.ISpendingDao;
import com.example.db.dao.SpendingDAO;
import com.example.entities.Payment;
import com.example.entities.Period;
import com.example.entities.User;
import com.example.exceptions.ServiceException;

public class DbSpendingService implements ISpendingService{

	private ISpendingDao spendingDAO;
	
	public DbSpendingService(){
		spendingDAO = new SpendingDAO(MyMoneyApp.getAppContext());
	}
	
	@Override
	public List<Payment> getUserPaymentsForPeriod(User currentUser, Period tablePeriod) throws ServiceException {
		try {
			return spendingDAO.getUserPaymentsForPeriod(currentUser,tablePeriod);
		} catch (DAOException e){
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean removePayment(User currentUser, Payment payment)	throws ServiceException {
		try {
			return spendingDAO.removePayment(currentUser,payment);
		} catch (DAOException e){
			throw new ServiceException(e);
		}
	}

	@Override
	public Payment getPaymentById(Long id) throws ServiceException{
		try {
			return spendingDAO.getPaymentById(id);
		} catch (DAOException e){
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean addSpending(User user, Payment payment) throws ServiceException{
		try {
			return spendingDAO.addSpending(user,payment);
		} catch (DAOException e){
			throw new ServiceException(e);
		}
	}

}
