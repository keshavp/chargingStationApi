
package com.scube.chargingstation.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.FileStorageException;
import com.scube.chargingstation.repository.BookingRequestRepository;
import com.scube.chargingstation.repository.CarModelRepository;
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
	CarModelRepository carModelRepository;
	
	@Autowired
	BookingRequestRepository bookingRequestRepository;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository; 
	  public FileStorageService(FileStorageProperties fileStorageProperties) 
	  {    
	  this.fileBaseLocation = fileStorageProperties.getUploadDir();
	  }
	 
		 public Resource loadFileAsResource( Long id , String imageFor ) throws Exception {
				
			 	String fileName ="";
			 	String newPAth = ""; 
	 			Path filePath = null ;
	 			
			 	try {
		        	
			 			if(imageFor.equals("CT")) {
			        		
			 				ChargerTypeEntity chargerTypeEntity  = chargerTypeRepository.findById(id.toString()).get();
			        		
			 				if(chargerTypeEntity==null)
			        		{
			        			throw BRSException.throwException("Error: Charger type request is invalid.");
			        		}
			 				
			        		fileName = chargerTypeEntity.getImagePath();   		
			        		logger.info("fileName===="+ fileName);
			        		
			 				newPAth = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_C_TYPE_DIR;
			 				logger.info("newPath===="+ newPAth);
			 				
			        		fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
			        		logger.info("fileStorageLocation===="+ fileStorageLocation);
			  	            
			  	            filePath = fileStorageLocation.resolve(fileName).normalize();
			  	            logger.info("filePath===="+ filePath);
			 			}
			 			
		  	          return getFileResource(filePath);
		        	
		        } catch (Exception ex) {
		            throw new Exception("File not found " + fileName, ex);
		        }
		    }
		 
		 public Resource loadReceiptFileAsResource( String id , String docFor) throws Exception {
				
			 	String fileName ="";
			 	String newPAth = "";
	 			Path filePath = null ; 

	 			try {
		        	
			 			if(docFor.equals("RC")) {
			 			
			 				ChargingRequestEntity chargingRequestEntity = chargingRequestRepository.findById(id).get();
			        		
			        		if(chargingRequestEntity==null)
			        		{
			        			throw BRSException.throwException("Error: Charging Request is invalid");
			        		}
			        		
			        		fileName = chargingRequestEntity.getInvoiceFilePath();  		
			        		logger.info("fileName===="+ fileName);
			 				
				 			newPAth = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_RECEIPT_DIR;
				 			logger.info("newPAth===="+ newPAth);
			        		
			        		fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
			        		logger.info("fileStorageLocation===="+ fileStorageLocation);
			  	            
			  	            filePath = fileStorageLocation.resolve(fileName).normalize();
			  	            logger.info("filePath===="+ filePath);
			  	            
			 			}
			 			if(docFor.equals("BRC")) {
				 			
			 				BookingRequestEntity bookingRequestEntity = bookingRequestRepository.findById(id).get();
			        		
			        		if(bookingRequestEntity==null)
			        		{
			        			throw BRSException.throwException("Error: Booking Request is invalid");
			        		}
			        		
			        		fileName = bookingRequestEntity.getInvoiceFilePath();  		
			        		logger.info("fileName===="+ fileName);
			 				
				 			newPAth = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_RECEIPT_DIR;
				 			logger.info("newPAth===="+ newPAth);
			        		
			        		fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
			        		logger.info("fileStorageLocation===="+ fileStorageLocation);
			  	            
			  	            filePath = fileStorageLocation.resolve(fileName).normalize();
			  	            logger.info("filePath===="+ filePath);
			  	            
			 			}
			 			
		 			 return getFileResource(filePath);
		        	
		        } catch (Exception ex) {
		            throw new Exception("File not found " + fileName, ex);
		        }
		    }
	
		 public Resource loadFileAsResource( String id , String imageFor) throws Exception {
				
			 	String fileName ="";
			 	String newPAth = "";
	 			Path filePath = null ; 
	 			
			 	try {
		        	
			 		if(imageFor.equals("CT")) {
		        		
		 				ChargerTypeEntity chargerTypeEntity  = chargerTypeRepository.findById(id).get();
		        		
		 				if(chargerTypeEntity==null)
		        		{
		        			throw BRSException.throwException("Error: Charger type request is invalid.");
		        		}
		 				
		        		fileName = chargerTypeEntity.getImagePath();   		
		        		logger.info("fileName===="+ fileName);
		        		
		 				newPAth = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_C_TYPE_DIR;
		 				logger.info("newPath===="+ newPAth);
		 				
		        		fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
		        		logger.info("fileStorageLocation===="+ fileStorageLocation);
		  	            
		  	            filePath = fileStorageLocation.resolve(fileName).normalize();
		  	            logger.info("filePath===="+ filePath);
		 			}
			 		
			 		// Uncomment for Car Model Image Upload
					/*
					 * else if(imageFor.equals("CMT")) {
					 * 
					 * CarModelEntity carModelEntity = carModelRepository.findById(id).get();
					 * 
					 * if(carModelEntity==null) { throw
					 * BRSException.throwException("Error: Car Model is Invalid"); }
					 * 
					 * fileName = carModelEntity.getImagePath(); logger.info("fileName===="+
					 * fileName);
					 * 
					 * newPAth = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_CM_TYPE_DIR;
					 * logger.info("newPath===="+ newPAth);
					 * 
					 * fileStorageLocation = Paths.get(newPAth).toAbsolutePath().normalize();
					 * logger.info("fileStorageLocation===="+ fileStorageLocation);
					 * 
					 * filePath = fileStorageLocation.resolve(fileName).normalize();
					 * logger.info("filePath===="+ filePath);
					 * 
					 * }
					 */ 
		  	        return getFileResource(filePath);
		        		
		        	
		        } catch (Exception ex) {
		            throw new Exception("File not found " + fileName, ex);
		        }
		    }
	
	
		 public Resource getFileResource(Path filePath) throws Exception {
		 
			 Resource resource  = new UrlResource(filePath.toUri());
	           if(resource.exists()) {
	           	logger.info("Inside IF(resource.exists)");
	               return resource;
	           } else {
	           	logger.info("Inside else()");
	               throw new Exception("File not found " + filePath);
	           }
		 
		 }

		public String storeFile(MultipartFile file, String imageFor) {
			// TODO Auto-generated method stub
			System.out.println("****fileStorageService storeFile*****"+file);
			
			Date date = new Date(System.currentTimeMillis());
			
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			String filename = fileName.split("\\.")[0];
			String extension = fileName.split("\\.")[1];
			
			String fileNewName = imageFor + "_" + date.getTime() + "." + extension;
			
			System.out.println(fileNewName);
			
			try {
				
				if(fileNewName.contains("..")) {
					throw new FileStorageException("Sorry! File Name contains invalid path sequence!");
				}
				String newPath;
				String returnPath = "";
				
				if(imageFor.equals("CT")) {
				
				/*if(flag.equalsIgnoreCase("2")) {
					newPath = this.fileAssociateBaseLocation + "/" + fileSubPath;
				}else {*/
					newPath = this.fileBaseLocation +"/"+UploadPathContUtils.FILE_C_TYPE_DIR; 
				//}
					
					 System.out.println("newPath=============" + newPath);
				
					 this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
				
					 Files.createDirectories(this.fileStorageLocation);
				
					 Path targetLocation = this.fileStorageLocation.resolve(fileNewName);
				
					 Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
				
					/*
					 * System.out.println("fileName" + fileNewName+ " ---filePath" +
					 * targetLocation);
					 * 
					 * returnPath = newPath + fileNewName;
					 * 
					 * System.out.println("returnPath=============" + returnPath);
					 */
				 
				}
				// Car Model
				/*
				 * else if(imageFor.equals("CMT")) { newPath = this.fileBaseLocation
				 * +"/"+UploadPathContUtils.FILE_CM_TYPE_DIR;
				 * 
				 * logger.info("New Path for Car Model ========>>>>" + newPath);
				 * 
				 * this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
				 * Files.createDirectories(this.fileStorageLocation);
				 * 
				 * Path targetLocation = this.fileStorageLocation.resolve(fileNewName);
				 * Files.copy(file.getInputStream(), targetLocation,
				 * StandardCopyOption.REPLACE_EXISTING); }
				 */
				
				return String.valueOf(fileNewName);
			}catch (IOException ex) {
				throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
			}
			
		
		}
}



