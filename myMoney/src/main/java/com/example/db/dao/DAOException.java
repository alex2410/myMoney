package com.example.db.dao;

public class DAOException extends Exception{

	public DAOException(String text) {
		super(text);
	}

	public DAOException(Throwable thr) {
		super(thr);
	}
}
