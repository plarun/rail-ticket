package reference.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTest {
    public static void main(String[] args) {
        final String receiver = "arunpandipava@gmail.com";
        final String sender = "arunpandipr@gmail.com";
        final String host = "smtp.gmail.com";
        final int port = 587;
        final String tls = "TLSv1.2";

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.protocols", tls);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, "aiskerhflpgttuns");
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            msg.setSubject("Test mail");
            msg.setText("Test msg body");

            System.out.println("sending...");

            Transport.send(msg);
            System.out.println("sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
