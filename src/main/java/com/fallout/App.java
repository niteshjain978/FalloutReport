package com.fallout;

import java.io.File;
import java.util.List;

public class App {
	
	public static void Start(String waveName, String inputPath) {
	
		try {
	     	
			String strCurrentDateTime = Helper.getCurrntDate().toString().replace(":","_").replace(" ","_");
	     	String currentFolder = inputPath + File.separator + strCurrentDateTime;
	     	Helper.createDirectory(currentFolder);
	     	List<File> listOffiles = Helper.listFilesInsideDir(inputPath);
	     	
	 	    for(File file : listOffiles) {
	     		Helper.convertCSVToMap(file);
	     	}
	     	
	 	    String outputFile = currentFolder +File.separator+waveName+"_Fallout_Report.csv";
	 	    Helper.generateReport(outputFile);
		} catch (Exception e){
			FalloutApplication.logger.info("Error : " + e.getMessage());
		}
	}
}
