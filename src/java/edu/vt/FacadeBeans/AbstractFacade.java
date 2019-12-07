/*
 * Created by Justin Kennedy on 2019.11.15  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import java.util.List;
/* 
 An instance of javax.persistence.EntityManager represents an Entity Manager.
 An Entity Manager manages JPA Entities. 
 Each Entity Manager instance is associated with a persistence context.
 A persistence context is a set of managed entity instances. 
- Balci
 */
import javax.persistence.EntityManager;

/**
 * The AbstractFacade.java is an abstract Facade class providing a generic interface to the Entity Manager.
 *
 * @author Justin Kennedy
 * @param <T> refers to the Class Type
 */
public abstract class AbstractFacade<T> {

    // An instance variable of the class object T
    private final Class<T> entityClass;

    /*  
    This is the constructor method called by the all of the subclass facade
    class's constructor methodvby passing the User, UserPhoto, and
    UserSurvey class respectively as a parameter.
    */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /* 
    This method is overridden in the subclasses to provide the actual implementation
    specific to the subclass
     */
    protected abstract EntityManager getEntityManager();

    // Stores the newly Created User, UserPhoto or UserSurvey (entity) object into the database.
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    // Stores the Edited User, UserPhoto or UserSurvey (entity) object into the database.
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    // Deletes (Removes) the given User, UserPhoto or UserSurvey (entity) object from the database.
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    // Finds a User, UserPhoto or UserSurvey in the database by using its Primary Key (id) and returns it.
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    // Returns a list of object references of all of the User, UserPhoto or UserSurvey entities in the database.
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    // Returns a List of User, UserPhoto or UserSurvey objects in a range from the database. 
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    // Obtains and returns the total number of User, UserPhoto or UserSurvey entities in the database.
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
