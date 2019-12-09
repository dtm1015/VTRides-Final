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

@Named("googleMapsManager")
@SessionScoped

/**
 *
 * @author jusmk96
 */
public class GoogleMapsManager implements Serializable {

    @Inject
    private AllRidesController allRidesController;
    private final String apiKey = "AIzaSyCquujdZ6xRKgTJF1qkYW6nghXtmqPw9ws";
    private final String directionsUrl = "https://www.google.com/maps/embed/v1/directions?key=";
    private final String tripInfoUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
    private int tripDistance;
    private int tripTime;

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
            int feet = distance.optInt("value",-1);
            AllRides ride = allRidesController.getSelected();
            if (seconds > 0){
                double minutesD = (double) seconds / 60;
                int minutes = (int)minutesD;
                tripTime = minutes;
                ride.setTripTime(minutes);
                //System.out.println(ride.getTripTime().toString());
            }
            if (feet > 0){
                double milesD = (double) feet / 5280;
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

    public String getDirections() {
        return directionsUrl + apiKey + "&origin="+getStartingAddress() + "&destination=" + getEndingAddress();
    }
}
