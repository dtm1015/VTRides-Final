/*
 * Created by Justin Kennedy on 2019.12.10  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.TimerService;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Named("timerController")
@SessionScoped

/**
 *
 * @author jusmk96
 */
public class TimerController implements Serializable {

    @Inject
    private EmergencyEmailController emailController;
    @Inject
    private AllRidesController allRidesController;

    private int currentTime = -1;
    private int maxTime;
    private boolean autoStart = false;
    private boolean timerGoing = false;

    public void incrementTime() throws MessagingException {
        if (currentTime != -1) {
            currentTime--;
            if (currentTime == 0) {
                emailController.sendSafetyNoEndingEmail();
            }
        }
    }

    public void setTimes() {
        maxTime = (int) ((int) allRidesController.getSelected().getTripTime() * 60 * 1.5);
        currentTime = maxTime;
        autoStart = true;
        timerGoing = true;
    }
    
    public void pauseTimes() throws IOException{
        autoStart = !autoStart;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    public void stopTimes(){
        autoStart = false;
        timerGoing = false;
        currentTime = -1;
    }

    public EmergencyEmailController getEmailController() {
        return emailController;
    }

    public void setEmailController(EmergencyEmailController emailController) {
        this.emailController = emailController;
    }

    public AllRidesController getAllRidesController() {
        return allRidesController;
    }

    public void setAllRidesController(AllRidesController allRidesController) {
        this.allRidesController = allRidesController;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public boolean isTimerGoing() {
        return timerGoing;
    }

    public void setTimerGoing(boolean timerGoing) {
        this.timerGoing = timerGoing;
    }
    
    

}
