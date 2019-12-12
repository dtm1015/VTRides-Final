/*
 * Created by Justin Kennedy on 2019.12.08  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.managers;

import edu.vt.EntityBeans.AllRides;
import edu.vt.controllers.AllRidesController;
import edu.vt.globals.Methods;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/*
-------------------------------------------------------------------------------
Within JSF XHTML pages, this bean will be referenced by using the name
'googleMapsManager'
-------------------------------------------------------------------------------
 */
@Named("googleMapsManager")

/*
 allRidesController will be session scoped, so the values of its instance variables
 will be preserved across multiple HTTP request-response cycles 
 */
@SessionScoped

/**
 *
 * @author jusmk96
 */
public class GoogleMapsManager implements Serializable {

    @Inject
    private AllRidesController allRidesController;
     /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private final String apiKey = "AIzaSyCquujdZ6xRKgTJF1qkYW6nghXtmqPw9ws";
    private final String directionsUrl = "https://www.google.com/maps/embed/v1/directions?key=";
    private final String tripInfoUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
    private final String latLongUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private final String priceApiKey = "rfej9napna.json";
    private final String priceUrl = "http://devapi.mygasfeed.com/stations/radius/";
    private int tripDistance;
    private int tripTime;

    private double startingLat;
    private double startingLong;
    private double endingLat;
    private double endingLong;
    private double gasPrice;
    private double pricePerPerson;
    
    /**
     * Sets trip time and distance from starting to ending address,
     * calculations performed by Google Maps API calls
     * 
     * @throws Exception if Maps database was not accessed correctly
     * @return true if Maps API database was accessed correctly, false
     * otherwise
     */
    public boolean getTripInfo() throws Exception {
        Methods.preserveMessages();
        String start = getStartingAddress();
        String end = getEndingAddress();
        
        try {
            String totalUrl = tripInfoUrl + start + "&destinations=" + end + "&key=" + apiKey;
            String urlResultsJsonData = readUrlContent(totalUrl);
            JSONObject resultsJsonObject = new JSONObject(urlResultsJsonData);
            JSONArray jsonArrayFoundObjects = resultsJsonObject.getJSONArray("rows");
            JSONObject onlyItem = jsonArrayFoundObjects.getJSONObject(0);
            JSONArray elements = onlyItem.getJSONArray("elements");
            JSONObject element = elements.getJSONObject(0);
            JSONObject duration = element.optJSONObject("duration");
            int seconds = duration.optInt("value", -1);
            JSONObject distance = element.optJSONObject("distance");
            int meters = distance.optInt("value",-1);
            AllRides ride = allRidesController.getSelected();
            //set trip time and distance
            if (seconds > 0){
                double minutesD = (double) seconds / 60;
                int minutes = (int)minutesD;
                tripTime = minutes;
                ride.setTripTime(minutes);
                //System.out.println(ride.getTripTime().toString());
            }
            if (meters > 0){
                double milesD = (double) meters / 1609.344;
                int miles = (int)milesD;
                tripDistance = miles;
                ride.setTripDistance(miles);
                //System.out.println(ride.getTripDistance().toString());
            }
            //allRidesController.update();
        } catch (Exception ex) {
            Methods.showMessage("Fatal Error", "The Google Maps Database was not accessed correctly.", "See: " + ex.getMessage());
            return false;
        }
        return true;
    }
     /*
    ===================
    Getters and Setters
    ===================
     */
    public AllRidesController getAllRidesController() {
        return allRidesController;
    }

    public void setAllRidesController(AllRidesController allRidesController) {
        this.allRidesController = allRidesController;
    }

    public int getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(int tripDistance) {
        this.tripDistance = tripDistance;
    }

    public int getTripTime() {
        return tripTime;
    }

    public void setTripTime(int tripTime) {
        this.tripTime = tripTime;
    }

    public double getStartingLat() {
        return startingLat;
    }

    public void setStartingLat(double startingLat) {
        this.startingLat = startingLat;
    }

    public double getStartingLong() {
        return startingLong;
    }

    public void setStartingLong(double startingLong) {
        this.startingLong = startingLong;
    }

    public double getEndingLat() {
        return endingLat;
    }

    public void setEndingLat(double endingLat) {
        this.endingLat = endingLat;
    }

    public double getEndingLong() {
        return endingLong;
    }

    public void setEndingLong(double endingLong) {
        this.endingLong = endingLong;
    }

    
    
    private String getStartingAddress() {
        String start;

        start = allRidesController.getSelected().getStartingAddress1().replace(" ", "+") + "+"
                + allRidesController.getSelected().getStartingCity().replace(" ", "+") + "+"
                + allRidesController.getSelected().getStartingState();

        return start;
    }

    private String getEndingAddress() {
        String end;
        

        end = allRidesController.getSelected().getEndingAddress1().replace(" ", "+") + "+"
                + allRidesController.getSelected().getEndingAddress2().replace(" ", "+") + "+"
                + allRidesController.getSelected().getEndingCity().replace(" ", "+") + "+"
                + allRidesController.getSelected().getEndingState();
        return end;
    }

    /**
     * Return the content of a given URL as String
     *
     * @param webServiceURL to retrieve the JSON data from
     * @return JSON data from the given URL as String
     * @throws Exception
     */
    public String readUrlContent(String webServiceURL) throws Exception {
        /*
        reader is an object reference pointing to an object instantiated from the BufferedReader class.
        Currently, it is "null" pointing to nothing.
         */
        BufferedReader reader = null;

        try {
            // Create a URL object from the webServiceURL given
            URL url = new URL(webServiceURL);
            /*
            The BufferedReader class reads text from a character-input stream, buffering characters
            so as to provide for the efficient reading of characters, arrays, and lines.
             */
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // Create a mutable sequence of characters and store its object reference into buffer
            StringBuilder buffer = new StringBuilder();

            // Create an array of characters of size 10240
            char[] chars = new char[10240];

            int numberOfCharactersRead;
            /*
            The read(chars) method of the reader object instantiated from the BufferedReader class
            reads 10240 characters as defined by "chars" into a portion of a buffered array.

            The read(chars) method attempts to read as many characters as possible by repeatedly
            invoking the read method of the underlying stream. This iterated read continues until
            one of the following conditions becomes true:

                (1) The specified number of characters have been read, thus returning the number of characters read.
                (2) The read method of the underlying stream returns -1, indicating end-of-file, or
                (3) The ready method of the underlying stream returns false, indicating that further input requests would block.

            If the first read on the underlying stream returns -1 to indicate end-of-file then the read(chars) method returns -1.
            Otherwise the read(chars) method returns the number of characters actually read.
            -Balci
             */
            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            // Return the String representation of the created buffer
            return buffer.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @return API query string for Directions API
     */
    public String getDirections() {
        return directionsUrl + apiKey + "&origin="+getStartingAddress() + "&destination=" + getEndingAddress();
    }
    
    /**
     * sets the latitude and longitude of the starting and ending addresses of the current trip
    */
    private void getLatLong(){
        String totalUrl1 = latLongUrl + getStartingAddress() + "&key=" + apiKey;
        String totalUrl2 = latLongUrl + getEndingAddress() + "&key=" + apiKey;
        Methods.preserveMessages();
        
        try {
            //access API 
            String urlResultsJsonData = readUrlContent(totalUrl1);
            //parse API info
            JSONObject resultsJsonObject = new JSONObject(urlResultsJsonData);
            JSONArray results = resultsJsonObject.getJSONArray("results");
            JSONObject oneResult = results.getJSONObject(0);
            JSONObject geometry1 = oneResult.getJSONObject("geometry");
            JSONObject location1 = geometry1.getJSONObject("location");
            double startLat = location1.optDouble("lat", 200);
            double startLong = location1.optDouble("lng", 200);
            
            if (startLat < 200){
                this.startingLat = startLat;
                //System.out.println(ride.getTripTime().toString());
            }
            if (startLong < 200){
                this.startingLong = startLong;
                //System.out.println(ride.getTripDistance().toString());
            }
            //allRidesController.update();
            
            String urlResultsJsonData2 = readUrlContent(totalUrl2);
            JSONObject resultsJsonObject2 = new JSONObject(urlResultsJsonData2);
            JSONArray results2 = resultsJsonObject2.getJSONArray("results");
            JSONObject oneResult2 = results2.getJSONObject(0);
            JSONObject geometry2 = oneResult2.getJSONObject("geometry");
            JSONObject location2 = geometry2.getJSONObject("location");
            double endLat = location2.optDouble("lat", 200);
            double endLong = location2.optDouble("lng", 200);
            
            if (endLat < 200){
                this.endingLat = endLat;
                //System.out.println(ride.getTripTime().toString());
            }
            if (endLong < 200){
                this.endingLong = endLong;
                //System.out.println(ride.getTripDistance().toString());
            }
        } catch (Exception ex) {
            Methods.showMessage("Fatal Error", "The Google Maps Database was not accessed correctly.", "See: " + ex.getMessage());
        }
        
    }
    
    /**
     * calculate gas price for current trip, set the trip price based on distance and gas price
     */
    public void getGasPrice(){
        getLatLong();
    
        gasPrice = 0;
        
        try {
            //access API
            String totalUrl = priceUrl + startingLat +"/" + startingLong + "/20/reg/price/" + priceApiKey;
            String urlResultsJsonData = readUrlContent(totalUrl);
            //parse API info
            JSONObject resultsJsonObject = new JSONObject(urlResultsJsonData);
            JSONArray stations = resultsJsonObject.getJSONArray("stations");
            if (stations.length() > 0){
                for(int i = 0; i < stations.length(); i++){
                    JSONObject object = stations.getJSONObject(i);
                    Double onePrice = object.optDouble("reg_price", -1);
                    if (onePrice > 0){
                        gasPrice = gasPrice + onePrice;
                    }
                }
            }
            gasPrice = gasPrice/stations.length();
            System.out.println("" + gasPrice);
            AllRides ride = allRidesController.getSelected();
            pricePerPerson = gasPrice * ride.getTripDistance() / (ride.getCarMpg() * allRidesController.numberOfRiders());
            //set the cost of the trip based on gas info
            ride.setTripCost((int)pricePerPerson);
            
            
            
        } catch (Exception ex) {
            Methods.showMessage("Fatal Error", "The Google Maps Database was not accessed correctly.", "See: " + ex.getMessage());
        }
    }
}
