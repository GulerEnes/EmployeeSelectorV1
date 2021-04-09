
package System;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaEmailSender {
    

    public static void sendMail(String recepient) throws Exception{
        System.out.println("Preparing to send email");
       Properties properties = new Properties(); 
       properties.put("mail.smtp.auth", "true");
       properties.put("mail.smtp.starttls.enable", "true");
       properties.put("mail.smtp.host", "smtp.gmail.com");
       properties.put("mail.smtp.port", "587");
       
       String myAccountMail="esin23800@gmail.com";
       String password="esin1999";
       Session session = Session.getInstance(properties,new Authenticator(){
         @Override
         protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication(myAccountMail,password);
         }
    });
       
       Message message = prepareMessage(session,myAccountMail,recepient);
       Transport.send(message);
       System.out.println("Message sent successfully");
    }
   

    private static Message prepareMessage(Session session, String myAccountMail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("WELCOME");
            message.setText("Welcome to the guide to finding employment and employee selection ");
            return message;
        } catch (Exception ex) {
            Logger.getLogger(JavaEmailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}

