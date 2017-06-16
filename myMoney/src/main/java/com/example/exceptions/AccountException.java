package com.example.exceptions;

public class AccountException extends ServiceException{

	public AccountException(String text) {
		super(text);
	}

	public AccountException(Throwable thr) {
		super(thr);
	}
}
