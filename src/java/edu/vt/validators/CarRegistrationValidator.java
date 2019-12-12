/*
 * Created by Quinton Miller on 2019.12.10  * 
 * Copyright © 2019 Osman Balci. All rights reserved. * 
 */
package edu.vt.validators;

import edu.vt.EntityBeans.AllRides;
import edu.vt.controllers.AllRidesController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import jdk.internal.org.xml.sax.XMLReader;
import static org.primefaces.component.hotkey.HotkeyBase.PropertyKeys.handler;
import org.xml.sax.helpers.XMLReaderFactory;

@FacesValidator("carRegistrationValidator")
public class CarRegistrationValidator implements Validator {
    
    @Override //VKJ1890 VA
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String fullValue = (String) value;
        String[] splitString = fullValue.split(" ");
        char[] stateChars = splitString[1].toCharArray();
        if (splitString.length != 2) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    //Check if there is a license number and a state abreviation
                    "Unrecognized Formatting!", "Please use the correct formatting for the license plate number!"));
        } else if (splitString[1].length() != 2) {
            //Chec if the abreviation is the right length
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Unrecognized Formatting!", "Please use the correct formatting for the license plate number!"));
        } else if (!Character.isUpperCase(stateChars[0]) || !Character.isUpperCase(stateChars[1])) {
            // Check if the abbreviation is all in upercase
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Unrecognized Formatting!", "Please use the correct formatting for the license plate number!"));
        } else if (splitString[0].contains("-")) {
            //Check if there is a dash in the license plate
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Unrecognized Formatting!", "Please use the correct formatting for the license plate number!"));
        } else {
            String licenseNumber = splitString[0];
            String stateAbbreviation = splitString[1];
            String username = "qemiller";
            String urlString = "http://www.vehicleregistrationapi.com/api/reg.asmx/CheckUSA?"
                    + "RegistrationNumber=" + licenseNumber
                    + "&State=" + stateAbbreviation
                    + "&username=" + username;

            try {
                URL request = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) request.openConnection();
                connection.setRequestMethod("GET");
                // To store our response
                StringBuilder content;
                Boolean found = false;

                // Get the input stream of the connection
                try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    found = true;
                    String line;
                    content = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        // Append each line of the response and separate them
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                } finally {
                    connection.disconnect();
                    if(found == false)
                    {
                        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                        "Invalid REgistration!", "The License plate you entered was not found!"));
                    }
                }

            } catch (MalformedURLException ex) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "Exception", ex.getMessage()));

            } catch (IOException ex) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "Exception", ex.getMessage()));
            }

        }

    }
}
