package com.scube.chargingstation.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.scube.chargingstation.util.FileStorageService;

import org.apache.commons.io.FileUtils;

@Service
public class DeleteFileServiceimpl implements DeleteFileService{
	
	@Value("${file.delete}")
	private String deletefile;
	
	@Value("${file.delete1}")
	private String deletefile1;
		
	public boolean deleteExcelfileDemo() throws IOException  {

		 String set45 = deletefile;
		 
		 String set46 = deletefile1;
		 
		 Date  curreDate = new Date(); 
		 String var23 = new SimpleDateFormat("yyyy-MM-dd").format(curreDate);
		 System.out.println("CurrentDatecheck:-"+var23);
	     LocalDate lDate = LocalDate.parse(var23);      
	     
	     LocalDate returnvalue = lDate.minusMonths(1);
	     System.out.println("minus date:-"+returnvalue);
	    	     
	     Date beforeCurrentDate = Date.from(returnvalue.atStartOfDay(ZoneId.systemDefault()).toInstant());


		 File f1[] = new File(set45).listFiles();    
		 
		 for (File filename : f1) {
				 
			 Date fileFolderDate = new Date(filename.lastModified()); 
 		
	            if (fileFolderDate.before(beforeCurrentDate)) {
	          
	            	filename.delete();

	            }
	            else {
	            	
	            	 System.out.println("\n"+"Date is within Range:-" + filename.getName()+"\n");
	            	 
	            }
		 }

	 
	 File f2[] = new File(set46).listFiles();    
		 
		 for (File filename2 : f2) {
				 
			 Date fileFolderDate2 = new Date(filename2.lastModified()); 
 		
	            if (fileFolderDate2.before(beforeCurrentDate)) {
	          
	            	filename2.delete();
	            }
	            else {
	            	
	            	 System.out.println("\n"+"Date2 is within Range:-" + filename2.getName()+"\n");
	            	 
	            }
		 }
		return true;	  
  
	}
	
}
