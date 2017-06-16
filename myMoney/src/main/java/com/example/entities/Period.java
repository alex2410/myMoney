package com.example.entities;

import java.util.Date;

import com.example.utils.DateUtils;

public class Period {

	private Date from;
	private Date to;

	public Period(Date from, Date to){
		this(from,to,true);
	}

	public Period(Date from, Date to, boolean roundDates) {
		this.from = from == null ? from : (roundDates ? DateUtils.getStartOfDay(from) : from);
		this.to = to == null ? to : (roundDates ? DateUtils.getEndOfDay(to) : to);
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "Period [from=" + from + ", to=" + to + "]";
	}
	
}
