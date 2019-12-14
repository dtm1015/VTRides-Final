/*
 * Created by Osman Balci on 2018.06.26
 * Copyright © 2018 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.globals.Methods;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
-------------------------------------------------------------------------------
Within JSF XHTML pages, this bean will be referenced by using the name
'textMessageController'
-------------------------------------------------------------------------------
 */
@Named(value = "textMessageController")

/*
 textMessageController will be session scoped, so the values of its instance variables
 will be preserved across multiple HTTP request-response cycles 
 */
@RequestScoped

/**
 * This class sends a Multimedia Messaging Service (MMS) Text Message to a cellular (mobile) phone.
 */
public class TextMessageController {

    /*
    ==================
    Constructor Method
    ==================
     */
    public TextMessageController() {
    }

    private String message;
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    Properties emailServerProperties;   // java.util.Properties
    Session emailSession;               // javax.mail.Session  
    MimeMessage mimeEmailMessage;       // javax.mail.internet.MimeMessage

    /*
    ================
    Instance Methods
    ================
     */
    public String getMessage() {   

        return message;
    }

    
    public void setMessage(String message) {
        this.message = message;
    }

    /*
    ============================================================
    Create Email Sesion and Transport Email in Plain Text Format
    ============================================================
     */
    public String sendTextMessage(String cellPhoneNumber, String cellPhoneCarrier) throws AddressException, MessagingException {
        // Set Email Server Properties
        emailServerProperties = System.getProperties();
        emailServerProperties.put("mail.smtp.port", "587");
        emailServerProperties.put("mail.smtp.auth", "true");
        emailServerProperties.put("mail.smtp.starttls.enable", "true");

        try {
            // Create an email session using the email server properties set above
            emailSession = Session.getDefaultInstance(emailServerProperties, null);

            /*
            Create a Multi-purpose Internet Mail Extensions (MIME) style email
            message from the MimeMessage class under the email session created.
            */
            mimeEmailMessage = new MimeMessage(emailSession);

            /*
            Specify the email address to send the email message containing the text message as
            
            5401234567@CellPhoneCarrier's MMS gateway domain
            
            The designated cell phone number will be charged for the text messaging by its carrier.
            Here are the MMS gateway domain names for some of the cell phone carriers and examples:
            
            mms.att.net     for AT&T            5401234567@mms.att.net
            pm.sprint.com   for Sprint          5401234567@pm.sprint.com
            tmomail.net     for T-Mobile        5401234567@tmomail.net
            vzwpix.com      for Verizon         5401234567@vzwpix.com
            vmpix.com       for Virgin Mobile   5401234567@vmpix.com
            */
            mimeEmailMessage.addRecipient(Message.RecipientType.TO, 
                    new InternetAddress(cellPhoneNumber + "@" + cellPhoneCarrier));

            /*
            Since some cell phones may not be able to process text messages in the HTML format,
            send the email message containing the text message in Plain Text format.
            */
            mimeEmailMessage.setContent(message, "text/plain");

            // Create a transport object that implements the Simple Mail Transfer Protocol (SMTP)
            Transport transport = emailSession.getTransport("smtp");

            /*
            Connect to Gmail's SMTP server using the username and password provided.
            For the Gmail's SMTP server to accept the unsecure connection, the
            Cloud.Software.Email@gmail.com account's "Allow less secure apps" option is set to ON.
            */
            transport.connect("smtp.gmail.com", "Cloud.Software.Email@gmail.com", "csd@VT-1872");

            // Send the email message containing the text message to the specified email address
            transport.sendMessage(mimeEmailMessage, mimeEmailMessage.getAllRecipients());

            // Close this service and terminate its connection
            transport.close();

            Methods.showMessage("Information", "Success!", "MMS Text Message is Sent!");
            

        } catch (AddressException ae) {
            Methods.showMessage("Fatal Error", "Email Address Exception Occurred!",
                    "See: " + ae.getMessage());
            
        } catch (MessagingException me) {
            Methods.showMessage("Fatal Error",
                    "Email Messaging Exception Occurred! Internet Connection Required!",
                    "See: " + me.getMessage());
        }
        return "/myRides/View?faces-redirect=true";
    }

}
