package com.example.ctyeung.capstonestage1.gmail;

import android.se.omapi.Session;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

/*
 * https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
 */
public class GMailSender extends javax.mail.Authenticator
{
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private javax.mail.Session session;

    static {
        Security.addProvider(new com.example.ctyeung.capstonestage1.gmail.JSSEProvider());
    }

    public GMailSender(String user,
                       String password)
    {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = javax.mail.Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject,
                                      String body,
                                      String sender,
                                      String recipients) throws Exception
    {
        try
        {
            MimeMessage  message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setContent(_multipart);
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        }
        catch(Exception e)
        {

        }
    }

    private Multipart _multipart = new MimeMultipart();

    public void addAttachment(String filename,String subject) throws Exception
    {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        _multipart.addBodyPart(messageBodyPart);

        BodyPart messageBodyPart2 = new MimeBodyPart();
        messageBodyPart2.setText(subject);

        _multipart.addBodyPart(messageBodyPart2);
    }

    public class ByteArrayDataSource implements DataSource
    {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type)
        {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data)
        {
            super();
            this.data = data;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getContentType()
        {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException
        {
            return new ByteArrayInputStream(data);
        }

        public String getName()
        {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException
        {
            throw new IOException("Not Supported");
        }
    }
}
