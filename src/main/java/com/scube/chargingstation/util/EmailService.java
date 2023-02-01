
  package com.scube.chargingstation.util;
  
  import java.util.Properties;
  
  import javax.validation.Valid; import javax.mail.Message; import
  javax.mail.MessagingException; import javax.mail.PasswordAuthentication;
  import javax.mail.Session; import javax.mail.Transport; import
  javax.mail.internet.InternetAddress; import javax.mail.internet.MimeBodyPart;
  import javax.mail.internet.MimeMessage;
  
  import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
  org.springframework.beans.factory.annotation.Value; import
  org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.CheckAllChargerStatusDto;
  
  @Service public class EmailService 
  {
  
  private static final Logger logger =
  LoggerFactory.getLogger(EmailService.class);
  
  @Value("${properties.emailport}") private String emailport;
  
  @Value("${properties.emailhost}") private String emailhost;
  
  @Value("${properties.accountDeleteEmail}") private String delEmailTo;
  
  @Value("${properties.emailfrom}") private String emailFrom;
  

  //@Value("${file.reset-password-link}") private String url;
  
  /*
  
  public void sendResetPasswordMail(@Valid String encodeEmail, @Valid String  email)
  {
  
  logger.info("*****EmailService sendResetPasswordMail*****");
  
  String to = email; String from = "hrmsscube@gmail.com";
  
  Properties properties = System.getProperties();
  properties.put("mail.smtp.host", emailhost); properties.put("mail.smtp.port",
  emailport); properties.put("mail.smtp.ssl.enable", "true");
  properties.put("mail.smtp.auth", "true");
  
  Session session = Session.getInstance(properties, new  javax.mail.Authenticator() 
  {
  
  protected PasswordAuthentication getPasswordAuthentication() {
  
  return new PasswordAuthentication("hrmsscube@gmail.com", "scube@123");
  
  }
  
  }); 
  
  session.setDebug(true);
  
  try {
  
  MimeMessage message = new MimeMessage(session); 
  MimeBodyPart textBodyPart =  new MimeBodyPart();
  
  // Set From: header field of the header. 
  message.setFrom(new  InternetAddress(from));
  
  // Set To: header field of the header.
  message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
  
  message.setSubject("Reset Password Link!");
  
  String vmFileContent
  ="Hello User, <br><br> We have received your reset password request .Please click link below to reset  your password. <br> "
  + "<a href='"+url+"ResetPassword?emailid="+
  encodeEmail+"'><strong>Reset Link</strong></a>";
  
  message.setText(vmFileContent,"UTF-8", "html");
  
  System.out.println("sending..."); // Send message Transport.send(message);
  
  // javaMailSender.send(message);
  System.out.println("Sent message successfully....");
  
  } catch (MessagingException e) { throw new RuntimeException(e); } 
  
  }*/
  
  
  public boolean accountDeleteEmail(@Valid String userMobileNo)
  {
  
  logger.info("*****EmailService accountDeleteEmail*****");
  
  String to = delEmailTo;
  
  Properties properties = System.getProperties();
  properties.put("mail.smtp.host", emailhost); 
  properties.put("mail.smtp.port", emailport); 
  properties.put("mail.smtp.ssl.enable", "true");
  properties.put("mail.smtp.auth", "true");
  
  Session session = Session.getInstance(properties, new  javax.mail.Authenticator() 
  {
  
  protected PasswordAuthentication getPasswordAuthentication() {
  
  return new PasswordAuthentication(emailFrom, "Dullhousi");
  
  }
  
  }); 
  
  session.setDebug(true);
  
  try {
  
  MimeMessage message = new MimeMessage(session); 
  MimeBodyPart textBodyPart =  new MimeBodyPart();
  
  // Set From: header field of the header. 
  message.setFrom(new  InternetAddress(emailFrom));
  
  // Set To: header field of the header.
  message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
  
  message.setSubject("Evdock account deletion request!");
  
  String vmFileContent  ="Hello User, <br><br> We have received a EvDock app account deletion request.<br> "
  		+ "User mobile number is : "+userMobileNo;
  
  message.setText(vmFileContent,"UTF-8", "html");
  
  System.out.println("sending..."); 
   //Send message 
   
  Transport.send(message);
  
  // javaMailSender.send(message);
  System.out.println("Sent message successfully....");
  
  } catch (MessagingException e) 
  { 
	 // throw new RuntimeException(e);
	  logger.info("*****MessagingException*****"+e.toString());

	  return false;
  
  }
 
  return true; 
  
  }
  
  public boolean sendEmailForAllChargerStatus(String emailContent, String thankYouContent, String currentDate) {
	  
	  logger.info("------ EmailService sendEmailForAllChargerStatus -------");
	  
	  String receipientAddress = delEmailTo;
	  
	  Properties properties = System.getProperties();
	  properties.put("mail.smtp.host", emailhost); 
	  properties.put("mail.smtp.port", emailport); 
	  properties.put("mail.smtp.ssl.enable", "true");
	  properties.put("mail.smtp.auth", "true");
	  
	  Session session = Session.getInstance(properties, new  javax.mail.Authenticator() {
		  protected PasswordAuthentication getPasswordAuthentication() {
			  return new PasswordAuthentication(emailFrom, "Dullhousi");  
		  }	  
	  }); 
	  
	  session.setDebug(true);
	  
	  try {
		  
		  MimeMessage message = new MimeMessage(session); 
		  
		  message.setFrom(new  InternetAddress(emailFrom));
		  message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipientAddress));
		  message.setSubject("Attention Required !!!");
		  
		  String vmFileContent = "Hello EV Dock, <br><br>" 
				  				+ " We are sharing the updated Charging Station and Charger Status.<br> "
				  				+ "The below issue was reported at around " + currentDate + "<br>"
		 	    				+ "PFB details for your reference." + "<br> "
		 	    				+ emailContent + thankYouContent ;
		  
		  message.setText(vmFileContent,"UTF-8", "html");
		  
		  logger.info("---- Sending in process -----");
		  
		  Transport.send(message);
		  
		  logger.info("---- Mail sent successfully -----");
		  
	  } 
	  catch (Exception e) {
		// TODO: handle exception
		  logger.info("------ MessagingException ------ " + e.toString());
		  return false;
	  }
	  
	  return false;
	  
  }
}