package edu.vt.controllers;

import edu.vt.EntityBeans.DefaultCar;
import edu.vt.EntityBeans.User;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.DefaultCarFacade;
import edu.vt.globals.Methods;


import java.lang.String;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/*
-------------------------------------------------------------------------------
Within JSF XHTML pages, this bean will be referenced by using the name
'defaultCarController'
-------------------------------------------------------------------------------
 */
@Named("defaultCarController")

/*
 defaultCarController will be session scoped, so the values of its instance variables
 will be preserved across multiple HTTP request-response cycles 
 */
@SessionScoped
public class DefaultCarController implements Serializable {

     /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    private String carMake;
    private String carModel;
    private String carColor;
    private String carLicenseNumber;
    private int carMPG;

    private edu.vt.FacadeBeans.DefaultCarFacade ejbFacade;
    private List<DefaultCar> items = null;
    private DefaultCar selected;


    /*
    The @EJB annotation implies that the EJB container will perform an injection of the object
    reference of the DefaultCarFacade object into carFacade when it is created at runtime.
     */

    @EJB
    private DefaultCarFacade carFacade;

    /*
    =================
    Contructor Method
    =================
     */
    public DefaultCarController() {
    }

    
    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public DefaultCar getSelected() {
        return selected;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarLicenseNumber() {
        return carLicenseNumber;
    }

    public void setCarLicenseNumber(String carLicenseNumber) {
        this.carLicenseNumber = carLicenseNumber;
    }

    public int getCarMPG() {
        return carMPG;
    }

    public void setCarMPG(int carMPG) {
        this.carMPG = carMPG;
    }

    public DefaultCarFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DefaultCarFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public DefaultCarFacade getCarFacade() {
        return carFacade;
    }

    public void setCarFacade(DefaultCarFacade carFacade) {
        this.carFacade = carFacade;
    }

    public void setSelected(DefaultCar selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DefaultCarFacade getFacade() {
        return ejbFacade;
    }


     /**
     * @param user is the entity corresponding to some user 
     * @return true if the user has previously specified a default car, false otherwise
     */
    public boolean hasDefaultCar(User user) {
        DefaultCar userCar = getCarFacade().findByUserid(user);
        if (userCar != null) {
            // A car already exists associated the signed in user.
            if (selected == null) {
                setSelected(userCar);
            }
            return true;
        } else {
            setSelected(null);
            return false;
        }
    }

    /**
     * creates default car associated with user 
     * @param user is the entity corresponding to some user 
     * @return redirect address for for the current user's profile page
     */
    public String createDefaultCar(User user) {
        Methods.preserveMessages();
        DefaultCar userCar = getCarFacade().findByUserid(user);
        if (userCar != null) {
            // A user already exists with the username entered by the user
            Methods.showMessage("Fatal Error", "You already have a default car!", "Click the edit button instead!");
            return "/userAccount/Profile.xhtml?faces-redirect=true";
        }
        //The user does not have a default car.
        try {
            DefaultCar newCar = new DefaultCar();
            newCar.setColor(carColor);
            newCar.setLicensePlate(carLicenseNumber);
            newCar.setMake(carMake);
            newCar.setMpg(carMPG);
            newCar.setModel(carModel);
            newCar.setUserId(user);

            getCarFacade().create(newCar);

        } catch (EJBException ex) {
            Methods.showMessage("Fatal Error", "Something went wrong while creating default car!",
                    "See: " + ex.getMessage());
            return "";
        }

        Methods.showMessage("Information", "Success!", "Default Car is Successfully Created!");

        return "/userAccount/Profile.xhtml?faces-redirect=true";
    }


    /**
     * edit default car of current user, redirect to profile page when done
     * @return redirect address for for the current user's profile page
     */
    public String updateDefaultCar() {
        Methods.preserveMessages();
        getCarFacade().edit(selected);
        return "/userAccount/Profile.xhtml?faces-redirect=true";
    }
    /**
     * prepare to create a new DefaultCar
     * @return newly created DefaultCar object, stored in selected
     */

    public DefaultCar prepareCreate() {
        selected = new DefaultCar();
        initializeEmbeddableKey();
        return selected;
    }
    
    /*
    ===============
    CRUD Operations
    ===============
     */
   
    public void create() {
        Methods.preserveMessages();
        
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DefaultCarCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        Methods.preserveMessages();
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DefaultCarUpdated"));

    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DefaultCarDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    /*
    ************************************************
    |   Other Auto Generated Methods by NetBeans   |
    ************************************************
     */
    public List<DefaultCar> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public DefaultCar getDefaultCar(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<DefaultCar> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DefaultCar> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DefaultCar.class)
    public static class DefaultCarControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DefaultCarController controller = (DefaultCarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "defaultCarController");
            return controller.getDefaultCar(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DefaultCar) {
                DefaultCar o = (DefaultCar) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DefaultCar.class.getName()});
                return null;
            }
        }

    }

}
