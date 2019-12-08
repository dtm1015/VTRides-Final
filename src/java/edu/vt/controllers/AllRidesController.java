package edu.vt.controllers;

import edu.vt.EntityBeans.AllRides;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.AllRidesFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

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
import org.primefaces.PrimeFaces;

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
    
    public void join() {
        Methods.preserveMessages();
        int joinId = userController.getSelected().getId();
        int isJoined = this.areJoined(joinId);
        // there are seats available and the user is not joined
        if (selected.getSeatsAvailable() > 0 && isJoined == 0){
            if (selected.getPasseger1Id() == null){
                selected.setPasseger1Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
            }
            else if (selected.getPassenger2Id() == null){
                selected.setPassenger2Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
            }
            else if (selected.getPassenger3Id() == null){
                selected.setPassenger3Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
            }
            else if (selected.getPassenger4Id() == null){
                selected.setPassenger4Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
            }
            else if (selected.getPassenger5Id() == null){
                selected.setPassenger5Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
            }
            else {
                selected.setPassenger6Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
            }
            this.update();
        }// joined on the ride already, needs to be unjoined
        else if (isJoined > 0){
            switch(isJoined){
                case 1: 
                    selected.setPasseger1Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    break;
                case 2: 
                    selected.setPassenger2Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    break;
                case 3: 
                    selected.setPassenger3Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    break;
                case 4: 
                    selected.setPassenger4Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    break;
                case 5: 
                    selected.setPassenger5Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    break;
                case 6: 
                    selected.setPassenger6Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    break;
            }
            this.update();
        }// not joined yet but no seats available
        else if(selected.getSeatsAvailable() == 0 && isJoined == 0){
            Methods.showMessage("Information", "Join Unsuccessful", "There are no seats available to join");
        }// something went wrong
        else {
            Methods.showMessage("Information", "Something Went Wrong", "");
        }
        items = null;
        items = this.getItems();
        
        /*
        Cases: 
        1) There is an open spot - check if they are joined, add them if they are not
        2) There is an open spot - check if they are joined, remove them if they are
        3) There are no open spots - check if they are joined, and do nothing if they are not
        4) There are no open spots - check if they are joined, remove them if they are
        */
    }
    
    private int areJoined(int passengerId){
        if (selected.getPasseger1Id() != null && selected.getPasseger1Id() == passengerId){
            return 1;
        }
        else if(selected.getPassenger2Id() != null && selected.getPassenger2Id() == passengerId){
            return 2;
        }
        else if(selected.getPassenger3Id() != null && selected.getPassenger3Id() == passengerId){
            return 3;
        }
        else if(selected.getPassenger4Id() != null && selected.getPassenger4Id() == passengerId){
            return 4;
        }
        else if(selected.getPassenger5Id() != null && selected.getPassenger5Id() == passengerId){
            return 5;
        }
        else if(selected.getPassenger6Id() != null && selected.getPassenger6Id() == passengerId){
            return 6;
        }
        else {
            return 0;
        }
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
