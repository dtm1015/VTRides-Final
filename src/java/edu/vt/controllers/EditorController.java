/*
 * Created by Osman Balci on 2018.06.26
 * Copyright © 2018 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/*
 ========================================
 PrimeFaces HTML Text Editor UI Component
 ========================================

 p:textEditor is an input UI component with rich text editing features. 
 See https://www.primefaces.org/showcase/ui/input/textEditor.xhtml

 The @Named class annotation designates the bean object created by this class 
 as a Contexts and Dependency Injection (CDI) managed bean. The object reference
 of a CDI-managed bean can be @Inject'ed in another CDI-Managed bean so that
 the other CDI-managed bean can access the methods and properties of this bean.

 Using the Expression Language (EL) in a JSF XHTML page, you can invoke a CDI-managed
 bean's method or set/get its property by using the logical name given with the 'value'
 parameter of the @Named annotation, e.g., #{editorController.emailMessage}
 */
@Named(value = "editorController")

/* 
 The @SessionScoped annotation indicates that this CDI-managed bean will be
 maintained (i.e., its property values will be kept) across multiple HTTP 
 requests as long as the user's established HTTP session is active. 
 */
@SessionScoped

public class EditorController implements Serializable {

    /*
     ============================
     Instance Variable (Property)
     ============================

     The emailMessage property is a String containing the entire email 
     message content in HTML format as a String of characters.
     */
    private String emailMessage;

    /*
    ==================
    Constructor Method
    ==================
     */
    public EditorController() {
        emailMessage = "";      // Initialize emailMessage
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String givenEmailMessage) {
        this.emailMessage = givenEmailMessage;
    }

    /*
    ===================
    Clear Email Content
    ===================
     */
    public void clearEmailContent() {

        emailMessage = "";
    }

}
