/*
 * Created by Justin Kennedy on 2019.12.10  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.globals.Methods;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author jusmk96
 */
public class EmergencyEmailController {
    //private String emailSubject;        // Subject line of the email message
    private String emailBody;           // Email content created in HTML format with PrimeFaces Editor
    

    Properties emailServerProperties;   // java.util.Properties
    Session emailSession;               // javax.mail.Session
    MimeMessage htmlEmailMessage;       // javax.mail.internet.MimeMessage
    /*
    ======================================================
    Create Email Sesion and Transport Email in HTML Format
    ======================================================
     */
    public void sendSafetyNoEndingEmail(User user, AllRidesController allRidesController) throws AddressException, MessagingException {
        
        // Obtain the email message content from the editorController object
        emailBody = this.setDefaultNoEndingEmail(user, allRidesController);

        // Email message content cannot be empty
        if (emailBody.isEmpty()) {
            Methods.showMessage("Error", "Please enter your email message!", "");
            return;
        }

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
            htmlEmailMessage = new MimeMessage(emailSession);

            // Set the email TO field to emailTo, which can contain only one email address
            htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmergencyContactEmail()));

            // It is okay for emailCc to be empty or null since the CC is optional

            // Set the email subject line
            htmlEmailMessage.setSubject("VTRides No End Trip Notification");

            // Set the email body to the HTML type text
            htmlEmailMessage.setContent(emailBody, "text/html");

            // Create a transport object that implements the Simple Mail Transfer Protocol (SMTP)
            Transport transport = emailSession.getTransport("smtp");

            /*
            Connect to Gmail's SMTP server using the username and password provided.
            For the Gmail's SMTP server to accept the unsecure connection, the
            Cloud.Software.Email@gmail.com account's "Allow less secure apps" option is set to ON.
             */
            transport.connect("smtp.gmail.com", "Cloud.Software.Email@gmail.com", "csd@VT-1872");

            
            // Send the htmlEmailMessage created to the specified list of addresses (recipients)
            transport.sendMessage(htmlEmailMessage, htmlEmailMessage.getAllRecipients());

            // Close this service and terminate its connection
            transport.close();

            Methods.showMessage("Information", "Success!", "Email Message is Sent!");

        } catch (AddressException ae) {
            Methods.showMessage("Fatal Error", "Email Address Exception Occurred!",
                    "See: " + ae.getMessage());

        } catch (MessagingException me) {
            Methods.showMessage("Fatal Error",
                    "Email Messaging Exception Occurred! Internet Connection Required!",
                    "See: " + me.getMessage());
        }
    }
    
    private String setDefaultNoEndingEmail(User user, AllRidesController allRidesController){
        
        String defaultBody = user.getFirstName() + " " + user.getLastName() 
                + " has NOT notified us that he/she has completed his/her trip from " + 
                allRidesController.getSelected().getStartingAddress1() + " " + 
                allRidesController.getSelected().getStartingCity() + ", " + 
                allRidesController.getSelected().getStartingState() + " " +
                allRidesController.getSelected().getStartingZipcode() + " to " + 
                allRidesController.getSelected().getEndingAddress1() + " " + 
                allRidesController.getSelected().getEndingCity() + ", " +
                allRidesController.getSelected().getEndingState() + " " + 
                allRidesController.getSelected().getEndingZipcode() + ".\n\n" + 
                "They are riding in a " + 
                allRidesController.getSelected().getCarColor() + " " + 
                allRidesController.getSelected().getCarMake() + " " +
                allRidesController.getSelected().getCarModel() + " with the license "
                + "plate number: " + allRidesController.getSelected().getCarLicensePlate() + "\n\n" +
                "The driver's name is " + allRidesController.getSelected().getDriverId().getFirstName() +
                " " + allRidesController.getSelected().getDriverId().getLastName() + ". The trip should take"
                + " about " + allRidesController.getSelected().getTripTime() + " minutes.";
        return defaultBody;
    }
}
