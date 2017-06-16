package com.example.services.spendings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.entities.Payment;
import com.example.entities.Payment.PaymentType;
import com.example.entities.Period;
import com.example.entities.User;
import com.example.exceptions.ServiceException;

public class SimpleSpendingService implements ISpendingService{

	private Map<User, List<Payment>> usersPayments = new HashMap<User, List<Payment>>();
	
	@Override
	public List<Payment> getUserPaymentsForPeriod(User currentUser, Period tablePeriod) throws ServiceException {
		List<Payment> list = usersPayments.get(currentUser);
		if(list == null){
			list = initDefaultPayments();
			usersPayments.put(currentUser, list);
		}
		ArrayList<Payment> lShow = new ArrayList<Payment>(list);
		Iterator<Payment> iterator =lShow.iterator();
		while(iterator.hasNext()){
			Payment next = iterator.next();
			if(tablePeriod.getFrom() != null && next.getCreateDate() != null) {
				if(tablePeriod.getFrom().after(next.getCreateDate())){
					iterator.remove();
					continue;
				}
			}
			if(tablePeriod.getTo() != null && next.getCreateDate() != null) {
				if(tablePeriod.getTo().before(next.getCreateDate())){
					iterator.remove();
					continue;
				}
			}
		}
		Collections.sort(lShow);
		return lShow;
	}

	private List<Payment> initDefaultPayments() {
		List<Payment> l = new ArrayList<Payment>();
		Payment p = new Payment();
		p.setId(0L);
		p.setName("test              212121 1");
		p.setType(PaymentType.SPENT);
		p.setValue(new BigDecimal("1231.222"));
		p.setCreateDate(new Date());
		l.add(p);
		
		p = new Payment();
		p.setId(1L);
		p.setName("test 2");
		p.setType(PaymentType.SPENT);
		p.setValue(new BigDecimal("22.01"));
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date());
		instance.set(Calendar.MONTH, 2);
		p.setCreateDate(instance.getTime());
		l.add(p);
		
		p = new Payment();
		p.setId(2L);
		p.setName("test 2");
		p.setType(PaymentType.SPENT);
		p.setValue(new BigDecimal("22.01"));
		instance.setTime(new Date());
		instance.set(Calendar.YEAR, 2014);
		p.setCreateDate(instance.getTime());
		l.add(p);
		return l;
	}

	@Override
	public boolean removePayment(User currentUser, Payment payment)	throws ServiceException {
		List<Payment> list = usersPayments.get(currentUser);
		if(list == null){
			return false;
		} else {
			return list.remove(payment);
		}
		
	}

	@Override
	public Payment getPaymentById(Long valueOf) throws ServiceException{
		Collection<List<Payment>> values = usersPayments.values();
		for(List<Payment> list : values){
			if(list == null){
				continue;
			}
			for(Payment p : list){
				if(valueOf.equals(p.getId())) {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public boolean addSpending(User currentUser, Payment p) throws ServiceException{
		List<Payment> list = usersPayments.get(currentUser);
		p.setId((long) list.size());
		return list.add(p);
	}

}
