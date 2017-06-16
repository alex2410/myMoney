package com.example.db.dao;

import java.util.List;

import com.example.entities.Payment;
import com.example.entities.Period;
import com.example.entities.User;

public interface ISpendingDao {

	List<Payment> getUserPaymentsForPeriod(User user, Period period) throws DAOException;

	boolean removePayment(User user, Payment payment) throws DAOException;

	Payment getPaymentById(Long id) throws DAOException;

	boolean addSpending(User user, Payment payment) throws DAOException;

}
