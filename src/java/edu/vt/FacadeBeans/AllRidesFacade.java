/*
 * Created by Justin Kennedy on 2019.12.06  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.AllRides;
import edu.vt.controllers.UserController;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jusmk96
 */
@Stateless
public class AllRidesFacade extends AbstractFacade<AllRides> {

    @PersistenceContext(unitName = "VTRidesTry3PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    @Inject
    private UserController userController;

    public AllRidesFacade() {
        super(AllRides.class);
    }
    
    /*
    *********************************************************
    The following method is added to the auto generated code.
    *********************************************************
     */
    /**
     * @param primaryKey is the Primary Key of the User entity in a table row in the database.
     * @return a list of photos associated with the User whose primary key is primaryKey
     * the primary key is the userId associated with the user whose photo you are looking for
     */
    public List<AllRides> findRidesByDriverId(Integer primaryKey) {

        return (List<AllRides>) em.createNamedQuery("AllRides.findByUserId")
                .setParameter("driverId", primaryKey)
                .getResultList();
    }
    
    public List<AllRides> findRidesByUserId(Integer primaryKey) {
        
        
        List<AllRides> driver = (List<AllRides>) em.createNamedQuery("AllRides.findByUserId")
                .setParameter("driverId", primaryKey)
                .getResultList();
        
        List<AllRides> passenger1 = (List<AllRides>) em.createNamedQuery("AllRides.findByPasseger1Id")
                .setParameter("passenger1Id", primaryKey)
                .getResultList();
        
        List<AllRides> passenger2 = (List<AllRides>) em.createNamedQuery("AllRides.findByPassenger2Id")
                .setParameter("passenger2Id", primaryKey)
                .getResultList();
        
        List<AllRides> passenger3 = (List<AllRides>) em.createNamedQuery("AllRides.findByPassenger3Id")
                .setParameter("passenger3Id", primaryKey)
                .getResultList();
        
        List<AllRides> passenger4 = (List<AllRides>) em.createNamedQuery("AllRides.findByPassenger4Id")
                .setParameter("passenger4Id", primaryKey)
                .getResultList();
        
        List<AllRides> passenger5 = (List<AllRides>) em.createNamedQuery("AllRides.findByPassenger5Id")
                .setParameter("passenger5Id", primaryKey)
                .getResultList();
        
        List<AllRides> passenger6 = (List<AllRides>) em.createNamedQuery("AllRides.findByPassenger6Id")
                .setParameter("passenger6Id", primaryKey)
                .getResultList();
        driver.addAll(passenger1);
        driver.addAll(passenger2);
        driver.addAll(passenger3);
        driver.addAll(passenger4);
        driver.addAll(passenger5);
        driver.addAll(passenger6);
        
        return driver;
    }
    
    public List<AllRides> findRidesStartingCity(String start) {

        return (List<AllRides>) em.createNamedQuery("AllRides.findByStartingCity")
                .setParameter("startingCity", start)
                .getResultList();
    }
    
    public List<AllRides> findRidesEndingCity(String end) {

        return (List<AllRides>) em.createNamedQuery("AllRides.findByEndingCity")
                .setParameter("endingCity", end)
                .getResultList();
    }
    
}
