/*
 * Created by Justin Kennedy on 2019.09.15
 * Copyright © 2019 Justin Kennedy. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.globals.Methods;
import javax.inject.Named;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
'emailController'
-------------------------------------------------------------------------------
 */
@Named(value = "emailController")
/*
 emailController will be session scoped, so the values of its instance variables
 will be preserved across multiple HTTP request-response cycles 
 */
@RequestScoped

public class EmailController {

    /*
    ==================
    Constructor Method
    ==================
     */
    public EmailController() {
    }

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    //private String emailTo;             // Contains only one email address to send email to
    //private String emailCc;             // Contains comma separated multiple email addresses with no spaces
    private String emailSubject;        // Subject line of the email message
    private String emailBody;           // Email content created in HTML format with PrimeFaces Editor

    Properties emailServerProperties;   // java.util.Properties
    Session emailSession;               // javax.mail.Session
    MimeMessage htmlEmailMessage;       // javax.mail.internet.MimeMessage

    /*
    ************************************************************************************************
    The import javax.inject.Inject; brings in the javax.inject package into our project. 
    "This package specifies a means for obtaining objects in such a way as to maximize 
    reusability, testability and maintainability compared to traditional approaches such as
    constructors, factories, and service locators (e.g., JNDI). This process, known as 
    dependency injection, is beneficial to most nontrivial applications." [Oracle] 
    
    The @Inject annotation of the instance variables:
    editorController
    allRidesCOntroller
    userController
    directs the CDI Container Manager to store the object reference of the EditorController,
    AllRidesController, and UserController classes' bean objects, after it is instantiated
    at runtime, into the instance variables given. 

    With this injection, the instance variables and instance methods of the EditorController,
    AllRidesController, and UserController classes can be accessed in this CDI-managed bean.
    ************************************************************************************************
     */
    @Inject
    private EditorController editorController;
    @Inject 
    private AllRidesController allRidesController;
    @Inject
    private UserController userController;
    
    private String defaultBody;
    

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public EditorController getEditorController() {
        return editorController;
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }

//    public String getEmailTo() {
//        return emailTo;
//    }
//
//    public void setEmailTo(String emailTo) {
//        this.emailTo = emailTo;
//    }
//
//    public String getEmailCc() {
//        return emailCc;
//    }
//
//    public void setEmailCc(String emailCc) {
//        this.emailCc = emailCc;
//    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    /*
    ======================================================
    Create Email Sesion and Transport Email in HTML Format
    ======================================================
     */
    public String emailPassengers() throws AddressException, MessagingException {

        Methods.preserveMessages();
        // Obtain the email message content from the editorController object
        emailBody = editorController.getEmailMessage();

        // Email message content cannot be empty
        if (emailBody.isEmpty()) {
            Methods.showMessage("Error", "Please enter your email message!", "");
            return "allRides/List?faces-redirect=true";
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
            htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(allRidesController.getSelected().getDriverId().getEmail()));

            String emailCc = allRidesController.getEmails();
            if (emailCc != null && !emailCc.isEmpty()) {
                /*
                Using the setRecipients method (as opposed to addRecipient),
                the CC field is set to contain multiple email addresses
                separated by comma with no spaces in the emailCc string given.
                 */
                htmlEmailMessage.setRecipients(Message.RecipientType.CC, emailCc);
            }
            // It is okay for emailCc to be empty or null since the CC is optional

            // Set the email subject line
            htmlEmailMessage.setSubject(emailSubject);

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
        return "allRides/List?faces-redirect=true";
    }
    
    
    /*
    ======================================================
    Create Email Sesion and Transport Email in HTML Format
    ======================================================
     */
    public void sendSafetyStartTripEmail() throws AddressException, MessagingException {

        // Obtain the email message content from the editorController object
        emailBody = this.setDefaultBeginEmail();

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
            htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userController.getEmergencyContactEmail()));

            // It is okay for emailCc to be empty or null since the CC is optional

            // Set the email subject line
            htmlEmailMessage.setSubject("VTRides Start Trip Notification");

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
    
    /*
    ======================================================
    Create Email Sesion and Transport Email in HTML Format
    ======================================================
     */
    public void sendSafetyNoEndingEmail() throws AddressException, MessagingException {

        // Obtain the email message content from the editorController object
        emailBody = this.setDefaultNoEndingEmail();

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
            htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userController.getEmergencyContactEmail()));

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
    
    /**
     * @return String containing the text to be sent via email to the emergency contact
     *          when the trip begins
     */
    private String setDefaultBeginEmail(){
        defaultBody = userController.getFirstName() + " " + userController.getLastName() 
                + " is beginning his/her trip from " + 
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
    
    /**
     * @return String containing the text to be sent via email to the emergency contact
     *          when the trip ends
     */
    private String setDefaultNoEndingEmail(){
        defaultBody = userController.getFirstName() + " " + userController.getLastName() 
                + " has not notified us that he/she has completed his/her trip from " + 
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
