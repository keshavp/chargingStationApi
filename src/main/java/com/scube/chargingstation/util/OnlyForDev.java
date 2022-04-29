package com.scube.chargingstation.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.scube.chargingstation.entity.ChargingRequestEntity;

// import com.scube.chargingstation.service.api.SmsService;

public class OnlyForDev {

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		/*
		 * PayslipPdfExporter payslipPdfExporter = new PayslipPdfExporter();
		 * 
		 * payslipPdfExporter.generatePdf();
		 */
		
	//	sendSMS	sendSMS = new sendSMS();
		
	//	sendSMS.sendSms();
		
	//	ChargingRequestEntity	chargingRequestEntity = new ChargingRequestEntity(); 
		
	//	PayslipPdfExporter payslipPdfExporter = new PayslipPdfExporter();
		
	//	chargingRequestEntity.getAmountCrDrStatus()
		
		
	//	payslipPdfExporter.generatePdf(chargingRequestEntity);
		
	/*
	 * SmsService smsService = new SmsService();
	 * 
	 * int verifymobiletoken = smsService.generateRandomMobileOTP();
	 * 
	 * String verifymobiletokenString = Integer.toString(verifymobiletoken);
	 * 
	 * String mobilenumber = "7506173236";
	 * 
	 * smsService.sendSignupOTPMobile(verifymobiletokenString,mobilenumber);
	 */
	
		
	/*
	 * System.out.println(RandomNumber.getRandomNumberString());
	 * System.out.println(RandomStringUtil.getAlphaNumericString(6, "keshav"));
	 * 
	 * 
	 * System.out.println(Math.round(466.89169491525426 * 100.0) / 100.0);
	 */
		
		sendSMS	sms = new sendSMS();
		
		sms.sendSms();
		
	}

	
}
