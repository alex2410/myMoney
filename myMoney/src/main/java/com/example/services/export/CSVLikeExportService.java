package com.example.services.export;

import java.util.List;

import com.example.exceptions.ServiceException;
import com.example.utils.helpers.FileHelper;

public class CSVLikeExportService implements IExportService<ICSVExportable> {

	private FileHelper fileHelper;
	
	public CSVLikeExportService(){
		fileHelper = new FileHelper();
	}

	@Override
	public String exportEntities(List<? extends ICSVExportable> entities, String fileName) throws ServiceException{
		StringBuilder sb = new StringBuilder();
		for(ICSVExportable p : entities){
			sb.append(p.toCSVlikeFormat()).append("\r\n");
		}
		fileHelper.writeToFile(sb,fileName);
		return "Экспортировано в файл "+fileName;
	}
}
