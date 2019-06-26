package com.sample;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailSSL {
	private static String emailid = "";
	private static String pwd = "";
	public static void main(String[] args) {
		//sendMail();
	}

	public  void sendMail(UserInfo user) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"ambika.arumugam2527@gmail.com", "priya@2527");
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ambika.arumugam2527@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("mahalakshmi8890@gmail.com"));
			message.setSubject("House Maintenace Request");
			message.setText("Hi Maha,"
					+ "\n\n You got the request for House Maintenance! "
					+ "\n\n" + user.getFname() + " " + user.getLname()
					+ " gave their details to contact them. " + "\n\n"
					+ "Contact them for further confirmation through \n"
					+ "Phone Number: " + user.getPhoneNo() + "\n" + "Email id: "
					+ user.getEmailId() + "\n" + "Address: " + user.getAddr()
					+"\n\n Regards,"+"\n Maha.");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public  void sendAcknowledgeMail(UserInfo user) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailid,pwd);
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailid));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmailId()));
			message.setSubject("House Maintenace Acknowledgement");
			message.setText("Hi "+user.getFname()+ " "+ user.getLname() +","
					+"\n\n We acknowledge your request. We'll contact you shortly!"+
					"\n\n Regards,"+"\n Maha.");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}