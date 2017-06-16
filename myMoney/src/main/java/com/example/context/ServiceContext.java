package com.example.context;

import java.util.HashMap;
import java.util.Map;

import com.example.MyMoneyApp;
import com.example.R;
import com.example.exceptions.ServiceException;
import com.example.services.IService;
import com.example.services.account.DbAccountService;
import com.example.services.account.IAccountService;
import com.example.services.export.CSVLikeExportService;
import com.example.services.export.IExportService;
import com.example.services.spendings.DbSpendingService;
import com.example.services.spendings.ISpendingService;

public final class ServiceContext {
	
	private static final Map<Class<? extends IService>, IService> SERVICES;
	
	static {
		SERVICES = new HashMap<Class<? extends IService>, IService>();
		initServices();
	}
	
	private ServiceContext(){
	}

	public static <T extends IService> T getService(Class<T> cls) throws ServiceException {
		T cast = cls.cast(SERVICES.get(cls));
		if(cast == null){
			throw new ServiceException(MyMoneyApp.getAppContext().getString(R.string.service_not_found)+cls.getName());
		}
		return cast;
	}

	private static void initServices() {
		SERVICES.put(IAccountService.class, new DbAccountService());
		SERVICES.put(ISpendingService.class, new DbSpendingService());
		SERVICES.put(IExportService.class, new CSVLikeExportService());
	}
}
