package com.example.utils.helpers;

import java.util.List;

import com.example.context.ServiceContext;
import com.example.entities.Payment;
import com.example.exceptions.ServiceException;
import com.example.services.export.IExportService;
import com.example.services.export.IExportable;

import android.app.Activity;
import android.util.Log;

public class ExportHelper {

	private Activity activity;

	public ExportHelper(Activity activity){
		this.activity = activity;
	}

	@SuppressWarnings("unchecked")
	public void export(List<Payment> paymentsForPeriod, String fileName) {
		try {
			IExportService<IExportable> service = ServiceContext.getService(IExportService.class);
			String exportEntities = service.exportEntities(paymentsForPeriod, fileName);
			if(!exportEntities.isEmpty()){
				ServiceException.showMessage(exportEntities, activity);
			}
		} catch (ServiceException e) {
			ServiceException.showMessage(e, activity);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Fail export");
			ServiceException.showMessage(new ServiceException(e), activity);
		}
	}
}
