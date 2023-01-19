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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DeleteFileServiceimpl implements DeleteFileService{
	
	@Value("${file.delete}")
	private String deletefile;
	
	@Value("${file.delete1}")
	private String deletefile1;
	
	@Value("${deletefile.frequency.indays}")
	private int daysToSubtract;
	
	private static final Logger logger = LoggerFactory.getLogger(DeleteFileServiceimpl.class);
		
	public boolean deleteExcelfileDemo()  {

		 String set45 = deletefile;
		 
		 String set46 = deletefile1;
		 
		 logger.info("file folder path:-"+set45+"---"+set46);
		 
		// int daysToSubtract;
		 
		 Date  curreDate = new Date(); 
		 String var23 = new SimpleDateFormat("yyyy-MM-dd").format(curreDate);
		 logger.info("CurrentDatecheck:-"+var23);
	     LocalDate lDate = LocalDate.parse(var23);      
	     
	     //LocalDate returnvalue = lDate.minusMonths(1);
	     
	     LocalDate returnvalue = lDate.minusDays(daysToSubtract);
	     logger.info("minus date:-"+returnvalue);
	    	     
	     Date beforeCurrentDate = Date.from(returnvalue.atStartOfDay(ZoneId.systemDefault()).toInstant());

try {
	
		
		

		 File f1[] = new File(set45).listFiles();    
		 
		 for (File filename : f1) {
				 
			 logger.info("11!");
			 Date fileFolderDate = new Date(filename.lastModified()); 
 		
	            if (fileFolderDate.before(beforeCurrentDate)) {
	          
	            	
	             	 logger.info("deleting folder 1 file");

	            	filename.delete();

	            }
	            else {
	            	
	            	 logger.info("folder 1 Date is within Range:-" + filename.getName()+"\n");
	            	 
	            }
		 }

	 
		
		  File f2[] = new File(set46).listFiles();
		  
		  for (File filename2 : f2) {
			  logger.info("22!");
		  Date fileFolderDate2 = new Date(filename2.lastModified());
		  
		  if (fileFolderDate2.before(beforeCurrentDate)) {
			  
         	 logger.info("deleting folder 2 file");
		  
		  filename2.delete(); 
		  } else 
		  {
		  
			  logger.info(" folder 2 Date2 is within Range:-" + filename2.getName()+"\n");
		  
		  } 
		  }
		 
		 
		
		
} catch (Exception e) {
	// TODO: handle exception
	logger.info("in delete file exception"+e.toString());
	
}	
return true;	
	}
	
}
