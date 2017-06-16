package com.example.services.export;

import java.util.List;

import com.example.exceptions.ServiceException;
import com.example.services.IService;

public interface IExportService<T extends IExportable> extends IService{

	String exportEntities(List<? extends T> entities, String fileName) throws ServiceException;
	
}
