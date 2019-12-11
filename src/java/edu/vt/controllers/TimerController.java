/*
 * Created by Justin Kennedy on 2019.12.10  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.TimerService;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

@Named("timerController")
@Stateless

/**
 *
 * @author jusmk96
 */
public class TimerController implements Serializable {
    
    @Resource
    private TimerService timerService;
    private Timer timer;
    private EmergencyEmailController emailController;
    private AllRidesController allRidesController;
    private User user;
    
    @Timeout
    public void timeoutHandler(Timer timer) throws MessagingException{
        emailController.sendSafetyNoEndingEmail(user, allRidesController);
    }
    
    public void startTimer(long time, User user, AllRidesController allRidesController){
        this.user = user;
        this.emailController = new EmergencyEmailController();
        this.allRidesController = allRidesController;
        TimerConfig config = new TimerConfig();
        config.setPersistent(false);
        timer = timerService.createSingleActionTimer(time, config);
    }
    
    public long getTime(){
        if (timer != null){
            return timer.getTimeRemaining();
        }
        return -1;
    }

    
    
    
    
}
