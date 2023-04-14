package com.scube.chargingstation.util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {
	
	 public static byte[] getQRCodeImage(HashMap<String, String> hashMap, int width, int height) {
	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        ObjectMapper mapper = new ObjectMapper();
	        
	        String text;
			try {
				text = mapper.writeValueAsString(hashMap);
	        
	        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

	        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	        MatrixToImageConfig con = new MatrixToImageConfig( 0xFF000002 , 0xFFFFFFFF ) ;

				MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
			
			
	        byte[] pngData = pngOutputStream.toByteArray();
	        return pngData;
	        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	    }

}
