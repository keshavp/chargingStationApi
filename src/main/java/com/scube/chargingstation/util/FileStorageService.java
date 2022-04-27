
package com.scube.chargingstation.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.util.FileStorageProperties;


@Service
public class FileStorageService {
	
	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	

	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
	 
	@Autowired
	ChargerTypeRepository chargerTypeRepository;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository;
	
	 
	  public FileStorageService(FileStorageProperties fileStorageProperties) 
	  {    
	  this.fileBaseLocation = fileStorageProperties.getUploadDir();
	  }
	 
		 public Resource loadFileAsResource( Long id) throws Exception {
				
			 	String fileName ="";
			 	try {
		        	
			 			String newPAth = "";     
		        		newPAth = this.fileBaseLocation;
		        	
		        		Optional<ChargerTypeEntity> cData = chargerTypeRepository.findById(id.toString());
		        		ChargerTypeEntity entity = cData.get();
		        		fileName = entity.getImagePath();   		
			        	
		        		fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
		  	          
		  	          	System.out.println("----this.fileStorageLocation--------userFor---------"+fileStorageLocation+"-------------");
		  	          
		  	            logger.info("newPath"+ newPAth);
		  	        	logger.info("fileStorageLocation"+ fileStorageLocation);
//		  	            System.out.println(this.fileStorageLocation);
		  	            
		  	            Path filePath = fileStorageLocation.resolve(fileName).normalize();
		  	            logger.info("filePath"+ filePath);
		  	            
		  	            Resource resource = new UrlResource(filePath.toUri());
		  	            if(resource.exists()) {
		  	            	logger.info("Inside IF(resource.exists)");
		  	                return resource;
		  	            } else {
		  	            	logger.info("Inside else()");
		  	                throw new Exception("File not found " + fileName);
		  	            }
		        		
		        	
		        } catch (Exception ex) {
		            throw new Exception("File not found " + fileName, ex);
		        }
		    }
		 
		 
		 
		 
		 public Resource loadReceiptFileAsResource( String id) throws Exception {
				
			 	String fileName ="";
			 	try {
		        	
			 			String newPAth = "";     
		        		newPAth = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_BOOKING_DIR;
		        	
		        		Optional<ChargingRequestEntity> cData = chargingRequestRepository.findById(id);
		        		if(cData==null)
		        		{
		        			throw BRSException.throwException("Error: Charging Request is invalid");
		        		}
		        		
		        		ChargingRequestEntity entity = cData.get();
		        		fileName = entity.getInvoiceFilePath();  		
			        	
		        		fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
		  	          
		  	          	System.out.println("----this.fileStorageLocation--------userFor---------"+fileStorageLocation+"-------------");
		  	          
		  	            logger.info("newPath"+ newPAth);
		  	        	logger.info("fileStorageLocation"+ fileStorageLocation);
//		  	            System.out.println(this.fileStorageLocation);
		  	            
		  	            Path filePath = fileStorageLocation.resolve(fileName).normalize();
		  	            logger.info("filePath"+ filePath);
		  	            
		  	            Resource resource = new UrlResource(filePath.toUri());
		  	            if(resource.exists()) {
		  	            	logger.info("Inside IF(resource.exists)");
		  	                return resource;
		  	            } else {
		  	            	logger.info("Inside else()");
		  	                throw new Exception("File not found " + fileName);
		  	            }
		        		
		        	
		        } catch (Exception ex) {
		            throw new Exception("File not found " + fileName, ex);
		        }
		    }
	
	
	

}
