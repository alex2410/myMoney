package com.example.utils.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileHelper {

	public void writeToFile(StringBuilder sb, String fileName) {
		File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if(sdCard != null && sdCard.exists()){
			File file = new File(sdCard,fileName); 
			FileWriter writer = null;
			try {
				writer = new FileWriter(file); 
				writer.write(sb.toString()); 
				writer.flush();
			} catch (Exception e) {
				Log.e(getClass().getName(), "Fail writeToFile file="+file.getAbsolutePath(), e);
			} finally {
				if(writer != null){
					try {
						writer.close();
					} catch (IOException e) {
						Log.e(getClass().getName(), "Fail close", e);
					}
				}
			}
		}
	}

}
