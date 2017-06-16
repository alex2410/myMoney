package com.example.services.spendings;

import java.util.List;

import com.example.entities.Payment;
import com.example.entities.Period;
import com.example.entities.User;
import com.example.exceptions.ServiceException;
import com.example.services.IService;

public interface ISpendingService extends IService {

    List<Payment> getUserPaymentsForPeriod(User currentUser, Period tablePeriod) throws ServiceException;

    boolean removePayment(User currentUser, Payment payment) throws ServiceException;

    Payment getPaymentById(Long valueOf) throws ServiceException;

    boolean addSpending(User currentUser, Payment p) throws ServiceException;

}
