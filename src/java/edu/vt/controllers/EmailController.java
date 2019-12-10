/*
 * Created by Osman Balci on 2018.06.26
 * Copyright © 2018 Osman Balci. All rights reserved.
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
 The @Named class annotation designates the bean object created by this class 
 as a Contexts and Dependency Injection (CDI) managed bean. The object reference
 of a CDI-managed bean can be @Inject'ed in another CDI-Managed bean so that
 the other CDI-managed bean can access the methods and properties of this bean.

 Using the Expression Language (EL) in a JSF XHTML page, you can invoke a CDI-managed
 bean's method or set/get its property by using the logical name given with the 'value'
 parameter of the @Named annotation, e.g., #{emailController.methodName() or property name}
 */
@Named(value = "emailController")
/* 
 The @RequestScoped annotation indicates that the user’s interaction with
 this CDI-managed bean will be active only in a single HTTP request.
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
    
    The @Inject annotation of the instance variable "private EditorController editorController;" 
    directs the CDI Container Manager to store the object reference of the EditorController class
    bean object, after it is instantiated at runtime, into the instance variable "editorController".

    With this injection, the instance variables and instance methods of the EditorController
    class can be accessed in this CDI-managed bean.
    ************************************************************************************************
     */
    @Inject
    private EditorController editorController;

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
    public void sendEmail(String emailTo) throws AddressException, MessagingException {

        // Obtain the email message content from the editorController object
        emailBody = editorController.getEmailMessage();

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
            htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

            // It is okay for emailCc to be empty or null since the CC is optional

            // Set the email subject line
            htmlEmailMessage.setSubject("VTRides Update");

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

}
