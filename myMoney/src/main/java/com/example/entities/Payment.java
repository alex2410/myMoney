package com.example.entities;

import java.math.BigDecimal;

import com.example.db.dao.BaseDao;
import com.example.utils.CurrencyUtils;
import com.example.utils.DateUtils;

import android.database.Cursor;
import android.util.Log;

public class Payment extends Entity implements Comparable<Payment>{
	
	private Long userId;
	private String name;
	private BigDecimal value;
	private PaymentType type;
	private String description;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public String getValueCurrency() {
		return CurrencyUtils.format(getValue(),"p ");
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}
	
	@Override
	public int compareTo(Payment another) {
		if(getCreateDate() == null) {
			return -1;
		}
		if(another.getCreateDate() == null) {
			return 1;
		}
		return getCreateDate().compareTo(another.getCreateDate());
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Payment [name=" + name + ", value=" + value + ", type=" + type
				+ ", description=" + description + ", Id=" + getId()
				+ ", createDate=" + getCreateDate() + "]";
	}
	
	@Override
	public String toCSVlikeFormat(){
		String separator = ",";
		StringBuilder sb = new StringBuilder();
		sb.append(getId()).append(separator).append(DateUtils.formatDate(getCreateDate())).append(separator)
			.append(userId).append(separator).append(userId).append(",\"").append(name.trim()).append("\",")
			.append(getValue()).append(separator).append(type).append(",\"").append(description).append("\"");
		//return getId()+","+DateUtils.formatDate(getCreateDate())+","+userId+",\""+name.trim()+"\","+
		//getValue().toString()+","+type+",\""+description+"\"";
		return sb.toString();
	}
	
	public enum PaymentType{
		GET("Получено"),
		SPENT("Потрачено");
		
		private String name;

		PaymentType(String name){
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}

		public static PaymentType getValueByName(String string) {
			for(PaymentType type : values()){
				if(type.toString().equals(string)){
					return type;
				}
			}
			return null;
		}
	}
	
	public static Payment adaptPayment(Cursor cursor){
		Payment p = new Payment();
		p.adapt(cursor);
		return p;
	}

	@Override
	public Payment adapt(Cursor cursor) {
		setId(cursor.getLong(2));
		setName(cursor.getString(0));
		setType(PaymentType.getValueByName(cursor.getString(5)));
		try {
			setValue(new BigDecimal(cursor.getString(4)));
		} catch (Exception e){
			Log.e(getClass().getName(), "Fail to parse "+cursor.getString(4), e);
			setValue(new BigDecimal(0));
		}
		setCreateDate(BaseDao.loadDate(cursor.getLong(1)));
		setDescription(cursor.getString(3));
		setUserId(cursor.getLong(6));
		return this;
	}

}
