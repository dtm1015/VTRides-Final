package edu.vt.controllers;

import edu.vt.EntityBeans.AllRides;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.AllRidesFacade;
import edu.vt.globals.Constants;

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
import javax.inject.Inject;

@Named("allRidesController")
@SessionScoped
public class AllRidesController implements Serializable {

    @EJB
    private edu.vt.FacadeBeans.AllRidesFacade ejbFacade;
    private List<AllRides> items = null;
    private List<AllRides> myItems = null;
    private List<AllRides> searchedItems = null;
    private AllRides selected;
    
    @Inject
    UserController userController;
    
    private String searchString;
    private String searchCategory;

    public AllRidesController() {
    }

    public AllRides getSelected() {
        return selected;
    }

    public void setSelected(AllRides selected) {
        this.selected = selected;
    }

    public AllRidesFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AllRidesFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }

    public List<AllRides> getMyItems() {
        myItems = getFacade().findRidesByUserId(userController.getSelected().getId());
        return myItems;
    }

    public void setMyItems(List<AllRides> myItems) {
        this.myItems = myItems;
    }

    public List<AllRides> getSearchedItems() {
        return searchedItems;
    }

    public void setSearchedItems(List<AllRides> searchedItems) {
        this.searchedItems = searchedItems;
    }
    
    public String search() {
        
        switch(searchCategory){
            case "startingCity":
                searchedItems = getFacade().findRidesStartingCity(searchString);
                break;
            case "endingCity":
                searchedItems = getFacade().findRidesEndingCity(searchString);  
        }
        
        return "/search/Results?faces-redirect=true";
    }
    /*
    **************************************
    Return List of U.S. State Postal Codes
    **************************************
     */
    public String[] listOfStates() {
        return Constants.STATES;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AllRidesFacade getFacade() {
        return ejbFacade;
    }

    public AllRides prepareCreate() {
        selected = new AllRides();
        selected.setDriverId(userController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AllRidesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AllRidesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AllRidesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AllRides> getItems() {
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

    public AllRides getAllRides(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<AllRides> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AllRides> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AllRides.class)
    public static class AllRidesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AllRidesController controller = (AllRidesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "allRidesController");
            return controller.getAllRides(getKey(value));
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
            if (object instanceof AllRides) {
                AllRides o = (AllRides) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AllRides.class.getName()});
                return null;
            }
        }

    }

}
