package com.scube.chargingstation.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Keshav Patel.
 */
public class RandomStringUtil {
    // function to generate a random string of length n
    public static String getAlphaNumericString(int n, String inputString) {

        // chose a Character random from this String
        String inputStringUcase = inputString.trim().toUpperCase().replaceAll(" ", "").concat("123456789");

        // create StringBuffer size of inputString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to inputString variable length
            int index
                    = (int) (inputStringUcase.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(inputStringUcase
                    .charAt(index));
        }

        return sb.toString();
    }
    
    
    
	/*
	 * public String getUniqueID(){ DateFormat dateFormat = new
	 * SimpleDateFormat("yyddmm"); Date date = new Date(); String
	 * dt=String.valueOf(dateFormat.format(date)); Calendar cal =
	 * Calendar.getInstance(); SimpleDateFormat time = new SimpleDateFormat("HHmm");
	 * String tm= String.valueOf(time.format(new Date()));//time in 24 hour format
	 * String id= dt+tm; System.out.println(id); return id; }
	 */
    
    
    
}