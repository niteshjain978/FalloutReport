package com.fallout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Helper {
	
	static Map<String,List<Map<String,String>>> finalMap = new HashMap<>();
	
	public static File createDirectory(String path) {
		File fileDirectory = new File(path);
        if (! fileDirectory.exists()){
        	fileDirectory.mkdir();
        	FalloutApplication.logger.info("Output Directory Created : " + path);
        } else {
        	FalloutApplication.logger.info("Output Directory already exits: " + path);
        }
        
        return fileDirectory;
	 }
	
	public static void convertCSVToMap(File inputFile) throws IOException {
		 String line = "";
		 try {
			 
			FalloutApplication.logger.info("Reading File : " + inputFile);
		    Scanner scnr = new Scanner(inputFile);
		    List<Map<String,String>> lineList = new LinkedList<>();
		    
	        while(scnr.hasNextLine()){
	             line = scnr.nextLine();
	             if(line.trim().equals("")) {
	            	 break;
	             }
	     
	            String str[] = line.split(",");
	            int site = 2;
	            int reason = 3;
	            Map<String,String> lineMap = new LinkedHashMap<>();
	            for(int i=0;i<str.length;i++){
	            	
	            	if (i==0){
	            		String arr[] = str[i].split("PROCESSING COMPLETED:");
	            		lineMap.put("Deal Id", arr[1].trim());
	            	}else if(i == 1) {
	            		String arr[] = str[i].split(":");
	            		if(!arr[1].trim().equals("")) {
	            			site++;
	            			reason++;
	            		}
	            	}else if (i == site) {
	            		String arr[] = str[i].split(":");
	            		lineMap.put("Site", arr[1].trim());
	            	} else if(i== reason) {
	            		if(str[i].contains("missing variables")) {
	            			lineMap.put("Fail Reason", "missing variables");
	            			String arr[] = str[i].split(":");
	            			lineMap.put("Description", arr[2].trim());
	            		}else {
	            			String arr[] = str[i].split(":");
	            			lineMap.put("Fail Reason", arr[2].trim() + " : " + arr[3].trim());
	            			lineMap.put("Description", "");
	            		}
	            	}
	            }
	            lineList.add(lineMap);
	        }
	        
	       scnr.close(); 
	       String sheetName = inputFile.getName().split("\\.")[0]; 
	       FalloutApplication.logger.info("sheetName :" + sheetName);
	       finalMap.put(sheetName, lineList); 
	       FalloutApplication.logger.info("Reading File Done : " + inputFile);
	       
	       } catch (Exception e) {
		    	FalloutApplication.logger.info("Error : " + line);
		    	FalloutApplication.logger.info("Error : " + e.getMessage());
		   }
	  }
	 
	 
	  public static void generateReport(String outputFile) throws IOException{
		FalloutApplication.logger.info("OutPutFile : " + outputFile);
       
		try {
		
			XSSFWorkbook workbook = new XSSFWorkbook(); 
	        for(Map.Entry<String, List<Map<String,String>>> outputMap : finalMap.entrySet()) {
			
				  String sheetName = outputMap.getKey();
			      List<Map<String,String>> list = outputMap.getValue();
			      XSSFSheet sheet = workbook.createSheet(sheetName);
			        
				  List<String> headers = list.stream()
				    		               .flatMap(map -> map.keySet().stream())
				    		               .distinct()
				    		               .collect(Collectors.toList());
				  
				  
				  int rownum = 0;
			      Row row = sheet.createRow(rownum++);
			      int cellnum = 0;
			      for (Object obj : headers){
			               Cell cell = row.createCell(cellnum++);
			               cell.setCellValue(obj.toString());
			       }
			      
			      for (Map<String, String> linemap : list) {
			    	  Row nrow = sheet.createRow(rownum++);
			    	  cellnum = 0;
			    	  for (Map.Entry<String, String> eachMap : linemap.entrySet()){
				               Cell cell = nrow.createCell(cellnum++);
				               Object ob = eachMap.getValue();
							   cell.setCellValue(ob.toString());
				      }
			       }
			      
		       }
				
			  //Write the workbook in file system
			  FileOutputStream out = new FileOutputStream(new File(outputFile),false);
			  workbook.write(out);
			  out.close();
	       } catch (Exception e) {
			   e.printStackTrace();
	       }
	 }
	 
	 public static String getCurrntDate() {
		 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
	     Date date = new Date(); 
	     return formatter.format(date).toString();
	 }
	 
	 public static  List<File> listFilesInsideDir(String dir) {
		    return new ArrayList<File>(Stream.of(new File(dir).listFiles())
		      .filter(file -> !file.isDirectory())
		      .filter(file -> file.getName().endsWith(".log_FO"))
		      .collect(Collectors.toSet()));
	  }
}
