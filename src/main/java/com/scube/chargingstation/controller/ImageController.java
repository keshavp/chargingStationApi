package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.service.ChargingPointService;
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
	
	@Autowired
	ChargingPointService	chargingPointService ; 
	
	 @GetMapping("/getImage/{imageFor}/{id}")
	 public ResponseEntity<byte[]> getFileFromStorageSelection(@PathVariable String id ,  @PathVariable String imageFor ) throws Exception {
		 
			 
			 Resource res =  fileStorageService.loadFileAsResource(id,imageFor);
			 
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
	 
	 @GetMapping("/getDoc/{docFor}/{id}")
	 public ResponseEntity<byte[]> getChargingRequestReceipt(@PathVariable String id ,  @PathVariable String docFor ) throws Exception {
		 
			 
			 Resource res =  fileStorageService.loadReceiptFileAsResource(id, docFor);
			 
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
	 
	 
	 @GetMapping("/getQRCode")
	 public ResponseEntity<byte[]> getQRCode() throws Exception {
		 
		 String medium="asdasdasd";
		 
		 ChargingPointDto chargingPointDto =  chargingPointService.getChargingPointById("1"); 
		 

		 ObjectMapper Obj = new ObjectMapper();
		 
		 String jsonStr = Obj.writeValueAsString(chargingPointDto);
		 
		 System.out.println(jsonStr); 
		 
		 
	        byte[] image = new byte[0];
	 //       try {

	            // Generate and Return Qr Code in Byte Array
	            // image = QRCodeGenerator.getQRCodeImage(medium,250,250);
	        
	        image = getQRCodeImage(jsonStr,250,250);
	        

	            // Generate and Save Qr Code Image in static/image folder
	  //          QRCodeGenerator.generateQRCodeImage(github,250,250,QR_CODE_IMAGE_PATH);

	  //      } catch (WriterException | IOException e) {
	  //          e.printStackTrace();
	  //      }
	        // Convert Byte Array into Base64 Encode String
	        String qrcode = Base64.getEncoder().encodeToString(image);

		 
			// byte[] bytes = StreamUtils.copyToByteArray(res.getInputStream());
			 
			 MediaType mediaType;
			 String ext = FilenameUtils.getExtension("PNG");
		        
		       /* if(ext.equalsIgnoreCase("pdf")|| ext == "PDF" ) {
		        
		        	mediaType = MediaType.APPLICATION_PDF ;
		        }
		        else {*/
		        	mediaType = MediaType.IMAGE_PNG ;
					/* } */
		        	
		        
		        return ResponseEntity.ok()
		                             .contentType(mediaType)
		                             .body(image);
	
	 }	
	 
	 
	  public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

	        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	        MatrixToImageConfig con = new MatrixToImageConfig( 0xFF000002 , 0xFFFFC041 ) ;

	        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
	        byte[] pngData = pngOutputStream.toByteArray();
	        return pngData;
	    }
	 
	
}
