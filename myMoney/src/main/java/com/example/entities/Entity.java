package com.example.entities;

import java.util.Date;

import com.example.services.export.ICSVExportable;
import com.example.utils.DateUtils;

import android.database.Cursor;

public abstract class Entity implements ICSVExportable{

	private Long id;
	private Date createDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public String toCSVlikeFormat(){
		return id+","+DateUtils.formatDate(createDate);
	}
	
	public abstract Entity adapt(Cursor cursor);
}
