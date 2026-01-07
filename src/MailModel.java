

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailModel {
	
	public boolean send (String from, String to, String subject, String content, List<String> fileNames) {
		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(content, "text/html");
			multipart.addBodyPart(bodyPart);
			
			if (!fileNames.isEmpty()) {
				for (var fileName : fileNames) {
					MimeBodyPart mimeBodyPart = new MimeBodyPart();
					DataSource dataSrc = new FileDataSource(fileName);
					mimeBodyPart.setDataHandler(new DataHandler(dataSrc));
					multipart.addBodyPart(mimeBodyPart);
				}
			}
			
			message.setContent(multipart);
			
			Transport.send(message);
			return true;
		}	catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean send (String from, String to, String subject, String content, String fileName) {
		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(content, "text/html");
			multipart.addBodyPart(bodyPart);
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			DataSource dataSrc = new FileDataSource(fileName);
			mimeBodyPart.setDataHandler(new DataHandler(dataSrc));
			multipart.addBodyPart(mimeBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			return true;
		}	catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean send(String from, String to, String subject, String content) {
		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(content);
			message.setContent(content, "text/html");
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	

	private Session getSession() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		Session session = Session.getDefaultInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("thanhdattt2006@gmail.com", "neth pxhe cmra vebm");
			}

		});
		return session;
	}
}