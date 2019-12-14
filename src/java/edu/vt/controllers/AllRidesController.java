package edu.vt.controllers;

import edu.vt.EntityBeans.AllRides;
import edu.vt.EntityBeans.DefaultCar;
import edu.vt.EntityBeans.User;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.AllRidesFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.managers.GoogleMapsManager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
    private UserController userController;
    
    @EJB
    private UserFacade userFacade;
    
     /*
    ************************************************************************************************
    The import javax.inject.Inject; brings in the javax.inject package into our project. 
    "This package specifies a means for obtaining objects in such a way as to maximize 
    reusability, testability and maintainability compared to traditional approaches such as
    constructors, factories, and service locators (e.g., JNDI). This process, known as 
    dependency injection, is beneficial to most nontrivial applications." [Oracle] 
    
    The @Inject annotation of the instance variables:
    googleMapsManager
    userController
  
    directs the CDI Container Manager to store the object reference of the GoogleMapsManager
    and UserController classes' bean objects, after it is instantiated at runtime, into the 
    instance variables given. 

    With this injection, the instance variables and instance methods of the GoogleMapsManager
    and UserController classes can be accessed in this CDI-managed bean.
    ************************************************************************************************
     */
    @Inject
    private GoogleMapsManager googleMapsManager;
        
    
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

        switch (searchCategory) {
            case "startingCity":
                searchedItems = getFacade().findRidesStartingCity(searchString);
                break;
            case "endingCity":
                searchedItems = getFacade().findRidesEndingCity(searchString);
        }

        return "/search/Results?faces-redirect=true";
    }

    /**
     * @return list of state abbreviations  
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

    public void fillInDefaultCar(DefaultCar userCar) {
        if (userCar != null) {
            this.selected.setCarMake(userCar.getMake());
            this.selected.setCarModel(userCar.getModel());
            this.selected.setCarColor(userCar.getColor());
            this.selected.setCarLicensePlate(userCar.getLicensePlate());
            this.selected.setCarMpg(userCar.getMpg());
        } else {
            Methods.showMessage("Error", "You don't have a default car!","Fill in the text boxes below.");
        }
    }

    public String getMapUrl() {
        return googleMapsManager.getDirections();
    }
/**
 * decides whether the map can be displayed given the available information
 * @return true if the relevant fields are not null, false otherwise 
 */
    public boolean canUseMap() {
        return this.selected != null
                && this.selected.getStartingAddress1() != null
                && this.selected.getStartingCity() != null
                && this.selected.getStartingState() != null
                && this.selected.getStartingZipcode() != null
                && this.selected.getEndingAddress1() != null
                && this.selected.getEndingCity() != null
                && this.selected.getEndingState() != null
                && this.selected.getEndingZipcode() != null;
    }
/**
 * updates passenger fields to indicate that a new rider has joined.
 * Cases: 
        1) There is an open spot - check if they are joined, add them if they are not
        2) There is an open spot - check if they are joined, remove them if they are
        3) There are no open spots - check if they are joined, and do nothing if they are not
        4) There are no open spots - check if they are joined, remove them if they are
 */
    public void join() {
        Methods.preserveMessages();
        int joinId = userController.getSelected().getId();
        int isJoined = this.areJoined(joinId);
        int initialCost = selected.getTripCost();
        int totalCost = this.numberOfRiders() * initialCost;
        // there are seats available and the user is not joined
        if (selected.getSeatsAvailable() > 0 && isJoined == 0) {
            if (selected.getPasseger1Id() == null) {
                selected.setPasseger1Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
                double newCost = totalCost / this.numberOfRiders();
                selected.setTripCost((int) newCost);
            } else if (selected.getPassenger2Id() == null) {
                selected.setPassenger2Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
                double newCost = totalCost / this.numberOfRiders();
                selected.setTripCost((int) newCost);
            } else if (selected.getPassenger3Id() == null) {
                selected.setPassenger3Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
                double newCost = totalCost / this.numberOfRiders();
                selected.setTripCost((int) newCost);
            } else if (selected.getPassenger4Id() == null) {
                selected.setPassenger4Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
                double newCost = totalCost / this.numberOfRiders();
                selected.setTripCost((int) newCost);
            } else if (selected.getPassenger5Id() == null) {
                selected.setPassenger5Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
                double newCost = totalCost / this.numberOfRiders();
                selected.setTripCost((int) newCost);
            } else {
                selected.setPassenger6Id(joinId);
                selected.setSeatsAvailable(selected.getSeatsAvailable() - 1);
                double newCost = totalCost / this.numberOfRiders();
                selected.setTripCost((int) newCost);
            }
            this.update();
        }// joined on the ride already, needs to be unjoined
        else if (isJoined > 0) {
            switch (isJoined) {
                case 1:
                    selected.setPasseger1Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    double newCost = totalCost / this.numberOfRiders();
                    selected.setTripCost((int) newCost);
                    break;
                case 2:
                    selected.setPassenger2Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    newCost = totalCost / this.numberOfRiders();
                    selected.setTripCost((int) newCost);
                    break;
                case 3:
                    selected.setPassenger3Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    newCost = totalCost / this.numberOfRiders();
                    selected.setTripCost((int) newCost);
                    break;
                case 4:
                    selected.setPassenger4Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    newCost = totalCost / this.numberOfRiders();
                    selected.setTripCost((int) newCost);
                    break;
                case 5:
                    selected.setPassenger5Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    newCost = totalCost / this.numberOfRiders();
                    selected.setTripCost((int) newCost);
                    break;
                case 6:
                    selected.setPassenger6Id(null);
                    selected.setSeatsAvailable(selected.getSeatsAvailable() + 1);
                    newCost = totalCost / this.numberOfRiders();
                    selected.setTripCost((int) newCost);
                    break;
            }
            this.update();
        }// not joined yet but no seats available
        else if (selected.getSeatsAvailable() == 0 && isJoined == 0) {
            Methods.showMessage("Information", "Join Unsuccessful", "There are no seats available to join");
        }// something went wrong
        else {
            Methods.showMessage("Information", "Something Went Wrong", "");
        }
        items = null;
        items = this.getItems();
    }
    /**
     * @param passengerId is the id associated with a potential passenger 
     * @return seat number of passengerId if the passenger has joined the ride, 0 otherwise
     */
    private int areJoined(int passengerId) {
        if (selected.getPasseger1Id() != null && selected.getPasseger1Id() == passengerId) {
            return 1;
        } else if (selected.getPassenger2Id() != null && selected.getPassenger2Id() == passengerId) {
            return 2;
        } else if (selected.getPassenger3Id() != null && selected.getPassenger3Id() == passengerId) {
            return 3;
        } else if (selected.getPassenger4Id() != null && selected.getPassenger4Id() == passengerId) {
            return 4;
        } else if (selected.getPassenger5Id() != null && selected.getPassenger5Id() == passengerId) {
            return 5;
        } else if (selected.getPassenger6Id() != null && selected.getPassenger6Id() == passengerId) {
            return 6;
        } else {
            return 0;
        }
    }
    /**
     * @return email addresses of each passenger, comma separated
     * in a single string
     */
    public String getEmails() {
        StringBuilder emails = new StringBuilder("");
        if (selected.getPasseger1Id() != null) {
            User passenger1 = userFacade.find(selected.getPasseger1Id());
            emails.append(passenger1.getEmail());
        }
        if (selected.getPassenger2Id() != null) {
            User passenger2 = userFacade.find(selected.getPassenger2Id());
            if (emails.length() > 0) {
                emails.append(",");
            }
            emails.append(passenger2.getEmail());
        }
        if (selected.getPassenger3Id() != null) {
            User passenger3 = userFacade.find(selected.getPassenger3Id());
            if (emails.length() > 0) {
                emails.append(",");
            }
            emails.append(passenger3.getEmail());
        }
        if (selected.getPassenger4Id() != null) {
            User passenger4 = userFacade.find(selected.getPassenger4Id());
            if (emails.length() > 0){
                emails.append(",");
            }
            emails.append(passenger4.getEmail());
        }
        if (selected.getPassenger5Id() != null) {
            User passenger5 = userFacade.find(selected.getPassenger5Id());
            if (emails.length() > 0){
                emails.append(",");
            }
            emails.append(passenger5.getEmail());
        }
        if (selected.getPassenger6Id() != null) {
            User passenger6 = userFacade.find(selected.getPassenger4Id());
            if (emails.length() > 0){
                emails.append(",");
            }
            emails.append(passenger6.getEmail());
        }
        return emails.toString();
    }
    
    /**
     *  @return number of filled seats on currently selected ride
     */
    public int numberOfRiders() {
        int numPeople = 1;
        if (selected.getPasseger1Id() != null) {
            numPeople++;
        }
        if (selected.getPassenger2Id() != null) {
            numPeople++;
        }
        if (selected.getPassenger3Id() != null) {
            numPeople++;
        }
        if (selected.getPassenger4Id() != null) {
            numPeople++;
        }
        if (selected.getPassenger5Id() != null) {
            numPeople++;
        }
        if (selected.getPassenger6Id() != null) {
            numPeople++;
        }
        return numPeople;
    }
    
    /**
     *  initializes selected ride and sets the driver ID to the current 
     * user ID
     * @return newly created AllRides object stored in selected
     */
    public AllRides prepareCreate() {
        selected = new AllRides();
        selected.setDriverId(userController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }
    
     /*
    ===============
    CRUD Operations
    ===============
     */
    
    /**
     * @throws Exception when creation fails
     */
    public void create() throws Exception {
        //if we have all the info needed to use Google Maps, store the trip info and gas price
        if (this.canUseMap()) {
            googleMapsManager.getTripInfo();
            googleMapsManager.getGasPrice();
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AllRidesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            //googleMapsManager.getTripInfo();
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
 /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
 */
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

    /*
    ************************************************
    |   Other Auto Generated Methods by NetBeans   |
    ************************************************
     */
    public AllRides getAllRides(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<AllRides> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AllRides> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public Date getEndTripDate(){
        long now = new Date().getTime();
        int milliseconds = this.selected.getTripTime() * 60000;
        Date end = new Date(now + milliseconds);
        return end;
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
