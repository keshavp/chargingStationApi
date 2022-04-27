package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.util.FileStorageService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/images"}, produces = APPLICATION_JSON_VALUE)
public class ImageController {

	
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired 
	ChargerTypeRepository chargerTypeRepository;
	
	@Autowired
	FileStorageService fileStorageService;
	
	 @GetMapping("/getCType/{id}")
	 public ResponseEntity<byte[]> getFileFromStorageSelection(@PathVariable Long id ) throws Exception {
		 
			 
			 Resource res =  fileStorageService.loadFileAsResource(id);
			 
			 byte[] bytes = StreamUtils.copyToByteArray(res.getInputStream());
			 
			 MediaType mediaType;
			 String ext = FilenameUtils.getExtension(res.getFilename());
		        
		        if(ext.equalsIgnoreCase("pdf")|| ext == "PDF" ) {
		        
		        	mediaType = MediaType.APPLICATION_PDF ;
		        }
		        else {
		        	mediaType = MediaType.IMAGE_JPEG ;
		        }
		        	
		        
		        return ResponseEntity.ok()
		                             .contentType(mediaType)
		                             .body(bytes);
	
	 }	
	 
	 @GetMapping("/getChargingRequestReceipt/{id}")
	 public ResponseEntity<byte[]> getChargingRequestReceipt(@PathVariable String id ) throws Exception {
		 
			 
			 Resource res =  fileStorageService.loadReceiptFileAsResource(id);
			 
			 byte[] bytes = StreamUtils.copyToByteArray(res.getInputStream());
			 
			 MediaType mediaType;
			 String ext = FilenameUtils.getExtension(res.getFilename());
		        
		        if(ext.equalsIgnoreCase("pdf")|| ext == "PDF" ) {
		        
		        	mediaType = MediaType.APPLICATION_PDF ;
		        }
		        else {
		        	mediaType = MediaType.IMAGE_JPEG ;
		        }
		        	
		        
		        return ResponseEntity.ok()
		                             .contentType(mediaType)
		                             .body(bytes);
	
	 }	
	 
	
}
