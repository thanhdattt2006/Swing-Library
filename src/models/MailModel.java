package models;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailModel {

    // Fix lỗi setSubject và tối ưu gửi HTML
    public boolean send(String from, String to, String subject, String content) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject); // Đã fix từ content sang subject
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm đọc template HTML từ file
    public String readTemplate(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private Session getSession() {
        Properties config = loadConfig();
        String user = config.getProperty("MAIL_USER");
        String pass = config.getProperty("MAIL_PASS");
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
    }

    private Properties loadConfig() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            System.err.println("Quên file config.properties kìa mày!");
        }
        return prop;
    }
}