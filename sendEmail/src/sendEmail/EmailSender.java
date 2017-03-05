package sendEmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class EmailSender{

	protected static final Logger LOGGER = Logger.getLogger(EmailSender.class);

	private static String hostName;
	private static String emailUserName;
	private static String emailPassword;
	private static String port;
/*	private static String auth;
	private static String starttls;
	
	
	
	private static String debug;
	private static String ssl;*/
	
	

	//private static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	public boolean sendMail(String from,String senderPass,String to[],String cc[], String subject,
			String body, String attachmentFilePath,String hostname,String smtpport,String auth,String starttls,String debug,String ssl) 
			throws NamingException, MessagingException, Exception {

		boolean retVal = false;
		hostName = hostname;
		port = smtpport;
		emailUserName = from;
		
		if(auth.equalsIgnoreCase("false")){
			emailPassword = null;
		}else{
			emailPassword = senderPass;
		}
		
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.starttls.enable", starttls);
			props.put("mail.smtp.ssl.enable",ssl);

			//The Session.getDefaultInstance method creates a new Session the first time it's called,
			//using the Properties that are passed. Subsequent calls will return that original Session and ignore any Properties you pass in.
			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(emailUserName, emailPassword);
						}
					});

			if (null != debug && debug.equals("true"))
				session.setDebug(true);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
            
			setTO(msg, to);
			setCC(msg, cc);

			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(new String(body.getBytes("UTF8"),"ISO-8859-1"), "text/html");
			
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp);
			msg.setContent(mp);
			
			msg.setSubject(subject);
			// code for final report
			if ((attachmentFilePath != null) && (!attachmentFilePath.equals(""))){
					mbp = new MimeBodyPart();
					FileDataSource fds1 = new FileDataSource(attachmentFilePath);
					mbp.setDataHandler(new DataHandler(fds1));
					mbp.setFileName((new File(attachmentFilePath).getName()));
					mp.addBodyPart(mbp);
			}
			Transport.send(msg);
			retVal = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Unable to send email. Please contact administrator.");
		}
		return retVal;
	}

	private void setCC(Message msg, String cc[]) throws AddressException,
			MessagingException {

		if (cc == null)
			return;

		if (cc.length > 1) {
			for (int i = 0; i < cc.length; i++) {
				msg.addRecipients(Message.RecipientType.CC, InternetAddress
						.parse(cc[i], false));
			}
		} else if (cc.length == 1) {
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(
					cc[0], false));
		}
	}

	private void setTO(Message msg, String to[]) throws AddressException,
			MessagingException {

		if (to == null)
			return;

		if (to.length > 1) {
			for (int i = 0; i < to.length; i++) {
				if (to[i] != null) {
					msg.addRecipients(Message.RecipientType.TO, InternetAddress
							.parse(to[i], false));
				}
			}
		} else if (to.length == 1) {
			if (to[0] != null) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress
						.parse(to[0], false));
			}
		}
	}
	
	public static void main(String[] args) {
		EmailSender emailManager = new EmailSender();

		Properties propLoader = new Properties();
		try {
			InputStream getpropertiesfile = new FileInputStream("EmailSender.properties");
			propLoader.load(getpropertiesfile);
		/*String fromEmail = args[0];
		String senderPass = args[1];
		String subject = args[2];
		String to[] = propLoader.getProperty("ToMail").split(",");
		String cc[] = propLoader.getProperty("CCMail").split(",");
		String message  = args[5]; 
		String attachmentFile = args[6];
		String hostname = args[7];//"smtp.gmail.com";
		String port = args[8];
		auth = args[9];//"true";
		starttls = args[10];//"true";
		debug = "true";
		ssl = "false";
*/		
		//"$MAILFR" "$SMTPPASS" "$SUBJECT" "$MAILTO" "$MAILCC" "$BODY" "$ATTACHMENT"  "$SMTPSERVER" "$SMTPPORT" "$AUTH" "$TLS"
		
		//String hostname = "smtp-mail.outlook.com";
		//String hostname = "smtp.mail.yahoo.com";
		//"25";//"587";//
		//String message = "<h1><b>Email HTML format Test!</b></h1><br><i>Greetings!</i><br><b>PI TEAM!</b><br><font color=red>Uniken India Pvt. ltd</font>";
		//String message = "על מנת להשלים את תהליך הרישום יש לספק את קוד האקטיבציה המופיע בתחתית";
		//String message = "मराठी ही इंडो-युरोपीय भाषाकुलातील एक भाषा आहे. भारतातील प्रमुख २२ भाषांपैकी मराठी एक आहे. महाराष्ट्र आणि गोवा ह्या राज्यांची मराठी ही अधिकृत राजभाषा आहे. मराठी मातृभाषा असणाऱ्या लोकसंख्येनुसार मराठी ही जगातील पंधरावी व भारतातील चौथी भाषा आहे."+"על מנת להשלים את תהליך הרישום יש לספק את קוד האקטיבציה המופיע בתחתית";
		
			boolean retval = emailManager.sendMail(propLoader.getProperty("FromEmail"),propLoader.getProperty("senderPass"),propLoader.getProperty("ToMail").split(","),propLoader.getProperty("CCMail").split(","),propLoader.getProperty("subject"),propLoader.getProperty("message"),propLoader.getProperty("attachmentFile"),propLoader.getProperty("hostname"),propLoader.getProperty("port"),propLoader.getProperty("auth"),propLoader.getProperty("starttls"),propLoader.getProperty("debug"),propLoader.getProperty("ssl"));
			if (retval) {
				LOGGER.info("Mail successfully sent");	
			}else {
				LOGGER.info("Sent Mail got failure");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
