package org.example.grudapp.ui;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotificationService {

  private String smtpHost;
  private String smtpPort;
  private String fromEmail;
  private String fromPassword;

  public EmailNotificationService(String smtpHost, String smtpPort, String fromEmail,
      String fromPassword) {
    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    this.fromEmail = fromEmail;
    this.fromPassword = fromPassword;
  }

  public void sendNotification(String toEmail, String subject, String body) {
    Properties props = new Properties();
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", smtpPort);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(fromEmail, fromPassword);
      }
    });

    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(fromEmail));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
      message.setSubject(subject);
      message.setText(body);

      Transport.send(message);
    } catch (MessagingException e) {
      System.out.println("Ошибка отправки уведомления: " + e.getMessage());
    }
  }
}
