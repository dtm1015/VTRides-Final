/*
 * Created by Justin Kennedy on 2019.12.06  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.DefaultCar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jusmk96
 */
@Stateless
public class DefaultCarFacade extends AbstractFacade<DefaultCar> {

    @PersistenceContext(unitName = "VTRidesTry3PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DefaultCarFacade() {
        super(DefaultCar.class);
    }
    
}
