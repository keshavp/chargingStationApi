 package com.scube.chargingstation.service;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.scube.chargingstation.controller.ContactUsController;
import com.scube.chargingstation.dto.incoming.ContactUsIncomingDto;

@Service
public class ContactUsServiceImpl implements ContactUsService {

	private static final Logger logger = LoggerFactory.getLogger(ContactUsServiceImpl.class);
	
	  @Value("${properties.emailport}") private String emailport;
	  
	  @Value("${properties.emailhost}") private String emailhost;
	  
	  @Value("${properties.accountContactUsEmail}") private String contactusEmailTo;
	  
	  @Value("${properties.emailfrom}") private String emailFrom;
	
	
	@Override
	public boolean getContactUs(@Valid ContactUsIncomingDto contactUsIncomingDto) {
		// TODO Auto-generated method stub
		
		
		
		
		
		
		
		return true;
	}
	
		
	@Override
	public boolean sendEmail(@Valid @RequestBody ContactUsIncomingDto contactUsIncomingDto) throws  Exception {

		
		String to = contactusEmailTo;

		logger.info("-------->1"+to);


		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", emailhost);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");

		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(emailFrom, "Dullhousi");


			}

		});
		logger.info("------>2");
		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			MimeBodyPart textBodyPart = new MimeBodyPart();

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(emailFrom));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("Contact Us request from website");

			// Now set the actual message     

			
			  String vmFileContent =
			  "Hello User,"+"\n  Name :- "+contactUsIncomingDto.getName()+"\n  Email :- "+contactUsIncomingDto.getEmail()+"\n   mobile_No :- "+
			  contactUsIncomingDto.getMobile_no()+"\n    Meesage :- "+
			  contactUsIncomingDto.getMessage();
			
//                		"Hello User, <br><br> We have received your reset password request .Please click link below to reset  your password.<br><a href='http://localhost:4200/resetPassword?emailId="+encodeEmail+"'><strong>Reset Link</strong></a> "+
//                        "<br><br><br> Thanks,<br>Team University";
			logger.info("------>3");
			message.setText(vmFileContent,"UTF-8", "html");
			
				System.out.println("sending...");
			// Send message
			Transport.send(message);

			// javaMailSender.send(message);
			System.out.println("Sent message successfully....");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
			return true;
	}
}
	
	

