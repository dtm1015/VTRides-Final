/*
 * Created by Justin Kennedy on 2019.09.15
 * Copyright © 2019 Justin Kennedy. All rights reserved.
 */
package edu.vt.controllers;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/*
-------------------------------------------------------------------------------
Within JSF XHTML pages, this bean will be referenced by using the name
'editorController'
-------------------------------------------------------------------------------
 */
@Named(value = "editorController")

/*
 editorController will be session scoped, so the values of its instance variables
 will be preserved across multiple HTTP request-response cycles 
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
