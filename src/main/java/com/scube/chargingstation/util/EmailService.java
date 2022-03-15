package com.scube.chargingstation.util;

import java.util.Properties;

import javax.validation.Valid;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	@Value("${properties.emailport}")
    private String emailport;
	
	@Value("${properties.emailhost}")
    private String emailhost;
	
	@Value("${file.reset-password-link}")
    private String url;

	public void sendResetPasswordMail(@Valid String encodeEmail, @Valid String email) {
		
		logger.info("*****EmailService sendResetPasswordMail*****");
		
		String to = email;
		String from = "hrmsscube@gmail.com";
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", emailhost);
		properties.put("mail.smtp.port", emailport);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

	                return new PasswordAuthentication("hrmsscube@gmail.com", "scube@123");

			}

		});
		session.setDebug(true);
		
		try {
			
			MimeMessage message = new MimeMessage(session);
			MimeBodyPart textBodyPart = new MimeBodyPart();
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			message.setSubject("Reset Password Link!");
			
			String vmFileContent ="Hello User, <br><br> We have received your reset password request .Please click link below to reset  your password. <br> "
					+ "<a href='"+url+"ResetPassword?emailid="+encodeEmail+"'><strong>Reset Link</strong></a>";
			
			message.setText(vmFileContent,"UTF-8", "html");
			
			System.out.println("sending...");
			// Send message
			Transport.send(message);

			// javaMailSender.send(message);
			System.out.println("Sent message successfully....");
			
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
