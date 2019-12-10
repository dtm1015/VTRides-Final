/*
 * Created by Justin Kennedy on 2019.12.06  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.DefaultCar;
import edu.vt.EntityBeans.User;
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
    
    public DefaultCar findByUserid(User userid) {
        if (em.createQuery("SELECT d FROM DefaultCar d WHERE d.userId = :userid")
                .setParameter("userid", userid)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (DefaultCar) (em.createQuery("SELECT d FROM DefaultCar d WHERE d.userId = :userid")
                    .setParameter("userid", userid)
                    .getSingleResult());
        }
    }
    
}
