package com.example.utils.helpers;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.example.context.ServiceContext;
import com.example.entities.Payment;
import com.example.entities.Period;
import com.example.entities.User;
import com.example.exceptions.ServiceException;
import com.example.services.spendings.ISpendingService;

import android.app.Activity;
import android.util.Log;

public class SpendingHelper {

	private Activity activity;
	private UserHelper userHelper;

	public SpendingHelper(Activity activity){
		this.activity = activity;
		userHelper = new UserHelper(activity);
	}

	public void addSpending(Payment p) {
		try {
			User user = userHelper.getCurrentUser();
			if(user != null){
				ISpendingService service = ServiceContext.getService(ISpendingService.class);
				service.addSpending(user,p);
			}
		} catch (ServiceException e) {
			ServiceException.showMessage(e,activity);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Fail add spending \n\r"+p);
			ServiceException.showMessage(new ServiceException(e),activity);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Payment> getPaymentsForPeriod(Period tablePeriod) {
		try {
			User currentUser = userHelper.getCurrentUser();
			if(currentUser != null){
				ISpendingService service = ServiceContext.getService(ISpendingService.class);
				return service.getUserPaymentsForPeriod(currentUser,tablePeriod);
			}
		} catch (ServiceException e) {
			ServiceException.showMessage(e, activity);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Fail getSpendingForPeriod\n\r"+tablePeriod);
			ServiceException.showMessage(new ServiceException(e),activity);
		}
		return Collections.EMPTY_LIST;
	}

	public boolean removePayment(Payment payment) {
		try {
			User currentUser = userHelper.getCurrentUser();
			ISpendingService service = ServiceContext.getService(ISpendingService.class);
			return service.removePayment(currentUser,payment);
		} catch (ServiceException e) {
			ServiceException.showMessage(e,activity);
			return false;
		} catch (Exception e) {
			Log.e(getClass().getName(), "Fail removePayment\n\r"+payment);
			ServiceException.showMessage(new ServiceException(e),activity);
			return false;
		}
	}

	public Payment getPaymentById(Long id) {
		try {
			ISpendingService service = ServiceContext.getService(ISpendingService.class);
			Payment payment = service.getPaymentById(id);
			return payment;
		} catch (ServiceException e) {
			ServiceException.showMessage(e, activity);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Fail getPaymentById id="+id+".");
			ServiceException.showMessage(new ServiceException(e),activity);
		}
		return null;
	}

	public Set<String> groupPaymentsByKey(List<Payment> payments) {
		Set<String> keys = new TreeSet<String>();
		for(Payment p : payments){
			if(p.getName() != null) {
				keys.add(p.getName().trim());
			}
		}
		return keys;
	}
}
