/*
 * Created by Quinton Miller on 2019.12.02  * 
 * Copyright © 2019 Osman Balci. All rights reserved. * 
 */
package edu.vt.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("phoneNumberValidator")

public class PhoneNumberValidator implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String number = (String) value;
        if (number.length() != 10) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Invalid Phone Number!",
                    "Phone Number is not long enough! Number must be 10 digits!"));
        }
        
        if (!number.matches("[0-9]+")){
               throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Unrecognized Phone Number!",
                    "Please enter only numbers and no spaces!"));
        }
       
         
    }
}
