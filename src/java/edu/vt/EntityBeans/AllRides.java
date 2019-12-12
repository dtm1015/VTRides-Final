/*
 * Created by Justin Kennedy on 2019.12.06  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.EntityBeans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jusmk96
 */
// The @Entity annotation designates this class as a JPA Entity class representing the AllRides table in the VTRidesDB database.
@Entity
// Name of the database table represented
@Table(name = "AllRides")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AllRides.findAll", query = "SELECT a FROM AllRides a")
    , @NamedQuery(name = "AllRides.findById", query = "SELECT a FROM AllRides a WHERE a.id = :id")
    , @NamedQuery(name = "AllRides.findByUserId", query = "SELECT a FROM AllRides a WHERE a.driverId.id = :driverId")
    , @NamedQuery(name = "AllRides.findByPasseger1Id", query = "SELECT a FROM AllRides a WHERE a.passeger1Id IS NOT NULL AND a.passeger1Id = :passenger1Id" )
    , @NamedQuery(name = "AllRides.findByPassenger2Id", query = "SELECT a FROM AllRides a WHERE a.passenger2Id IS NOT NULL AND a.passenger2Id = :passenger2Id")
    , @NamedQuery(name = "AllRides.findByPassenger3Id", query = "SELECT a FROM AllRides a WHERE a.passenger3Id IS NOT NULL AND a.passenger3Id = :passenger3Id")
    , @NamedQuery(name = "AllRides.findByPassenger4Id", query = "SELECT a FROM AllRides a WHERE a.passenger4Id IS NOT NULL AND a.passenger4Id = :passenger4Id")
    , @NamedQuery(name = "AllRides.findByPassenger5Id", query = "SELECT a FROM AllRides a WHERE a.passenger5Id IS NOT NULL AND a.passenger5Id = :passenger5Id")
    , @NamedQuery(name = "AllRides.findByPassenger6Id", query = "SELECT a FROM AllRides a WHERE a.passenger6Id IS NOT NULL AND a.passenger6Id = :passenger6Id")
    , @NamedQuery(name = "AllRides.findBySeatsAvailable", query = "SELECT a FROM AllRides a WHERE a.seatsAvailable = :seatsAvailable")
    , @NamedQuery(name = "AllRides.findByStartingAddress1", query = "SELECT a FROM AllRides a WHERE a.startingAddress1 = :startingAddress1")
    , @NamedQuery(name = "AllRides.findByStartingAddress2", query = "SELECT a FROM AllRides a WHERE a.startingAddress2 = :startingAddress2")
    , @NamedQuery(name = "AllRides.findByStartingCity", query = "SELECT a FROM AllRides a WHERE a.startingCity = :startingCity")
    , @NamedQuery(name = "AllRides.findByStartingState", query = "SELECT a FROM AllRides a WHERE a.startingState = :startingState")
    , @NamedQuery(name = "AllRides.findByStartingZipcode", query = "SELECT a FROM AllRides a WHERE a.startingZipcode = :startingZipcode")
    , @NamedQuery(name = "AllRides.findByEndingAddress1", query = "SELECT a FROM AllRides a WHERE a.endingAddress1 = :endingAddress1")
    , @NamedQuery(name = "AllRides.findByEndingAddress2", query = "SELECT a FROM AllRides a WHERE a.endingAddress2 = :endingAddress2")
    , @NamedQuery(name = "AllRides.findByEndingCity", query = "SELECT a FROM AllRides a WHERE a.endingCity = :endingCity")
    , @NamedQuery(name = "AllRides.findByEndingState", query = "SELECT a FROM AllRides a WHERE a.endingState = :endingState")
    , @NamedQuery(name = "AllRides.findByEndingZipcode", query = "SELECT a FROM AllRides a WHERE a.endingZipcode = :endingZipcode")
    , @NamedQuery(name = "AllRides.findByTripTime", query = "SELECT a FROM AllRides a WHERE a.tripTime = :tripTime")
    , @NamedQuery(name = "AllRides.findByTripDistance", query = "SELECT a FROM AllRides a WHERE a.tripDistance = :tripDistance")
    , @NamedQuery(name = "AllRides.findByTripCost", query = "SELECT a FROM AllRides a WHERE a.tripCost = :tripCost")
    , @NamedQuery(name = "AllRides.findByTripDate", query = "SELECT a FROM AllRides a WHERE a.tripDate = :tripDate")
    , @NamedQuery(name = "AllRides.findByCarMake", query = "SELECT a FROM AllRides a WHERE a.carMake = :carMake")
    , @NamedQuery(name = "AllRides.findByCarModel", query = "SELECT a FROM AllRides a WHERE a.carModel = :carModel")
    , @NamedQuery(name = "AllRides.findByCarColor", query = "SELECT a FROM AllRides a WHERE a.carColor = :carColor")
    , @NamedQuery(name = "AllRides.findByCarLicensePlate", query = "SELECT a FROM AllRides a WHERE a.carLicensePlate = :carLicensePlate")
    , @NamedQuery(name = "AllRides.findByCarMpg", query = "SELECT a FROM AllRides a WHERE a.carMpg = :carMpg")
})
public class AllRides implements Serializable {

    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the AllRides table in the VTRidesDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "passeger_1_id")
    private Integer passeger1Id;
    @Column(name = "passenger_2_id")
    private Integer passenger2Id;
    @Column(name = "passenger_3_id")
    private Integer passenger3Id;
    @Column(name = "passenger_4_id")
    private Integer passenger4Id;
    @Column(name = "passenger_5_id")
    private Integer passenger5Id;
    @Column(name = "passenger_6_id")
    private Integer passenger6Id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seats_available")
    private int seatsAvailable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "startingAddress1")
    private String startingAddress1;
    @Size(max = 128)
    @Column(name = "startingAddress2")
    private String startingAddress2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "startingCity")
    private String startingCity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "startingState")
    private String startingState;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "startingZipcode")
    private String startingZipcode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "endingAddress1")
    private String endingAddress1;
    @Size(max = 128)
    @Column(name = "endingAddress2")
    private String endingAddress2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "endingCity")
    private String endingCity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "endingState")
    private String endingState;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "endingZipcode")
    private String endingZipcode;
    @Column(name = "tripTime")
    private Integer tripTime;
    @Column(name = "tripDistance")
    private Integer tripDistance;
    @Column(name = "tripCost")
    private Integer tripCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tripDate")
    @Temporal(TemporalType.DATE)
    private Date tripDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "carMake")
    private String carMake;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "carModel")
    private String carModel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "carColor")
    private String carColor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "carLicensePlate")
    private String carLicensePlate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carMpg")
    private int carMpg;
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @ManyToOne
    private User driverId;
    
     /*
    =============================================================
    Class constructors for instantiating a AllRides entity object to
    represent a row in the User's table in the VTRidesDB database.
    =============================================================
     */
    public AllRides() {
    }
    
    public AllRides(User user){
        this.driverId = user;
    }

    public AllRides(Integer id, User user) {
        this.id = id;
        this.driverId = user;
    }

    public AllRides(Integer id, User user, int seatsAvailable, String startingAddress1, String startingCity, String startingState, String startingZipcode, String endingAddress1, String endingCity, String endingState, String endingZipcode, Date tripDate, String carMake, String carModel, String carColor, String carLicensePlate, int carMpg) {
        this.id = id;
        this.driverId = user;
        this.seatsAvailable = seatsAvailable;
        this.startingAddress1 = startingAddress1;
        this.startingCity = startingCity;
        this.startingState = startingState;
        this.startingZipcode = startingZipcode;
        this.endingAddress1 = endingAddress1;
        this.endingCity = endingCity;
        this.endingState = endingState;
        this.endingZipcode = endingZipcode;
        this.tripDate = tripDate;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carLicensePlate = carLicensePlate;
        this.carMpg = carMpg;
    }

    /*
    =========================
    Getter and Setter methods 
    =========================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPasseger1Id() {
        return passeger1Id;
    }

    public void setPasseger1Id(Integer passeger1Id) {
        this.passeger1Id = passeger1Id;
    }

    public Integer getPassenger2Id() {
        return passenger2Id;
    }

    public void setPassenger2Id(Integer passenger2Id) {
        this.passenger2Id = passenger2Id;
    }

    public Integer getPassenger3Id() {
        return passenger3Id;
    }

    public void setPassenger3Id(Integer passenger3Id) {
        this.passenger3Id = passenger3Id;
    }

    public Integer getPassenger4Id() {
        return passenger4Id;
    }

    public void setPassenger4Id(Integer passenger4Id) {
        this.passenger4Id = passenger4Id;
    }

    public Integer getPassenger5Id() {
        return passenger5Id;
    }

    public void setPassenger5Id(Integer passenger5Id) {
        this.passenger5Id = passenger5Id;
    }

    public Integer getPassenger6Id() {
        return passenger6Id;
    }

    public void setPassenger6Id(Integer passenger6Id) {
        this.passenger6Id = passenger6Id;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getStartingAddress1() {
        return startingAddress1;
    }

    public void setStartingAddress1(String startingAddress1) {
        this.startingAddress1 = startingAddress1;
    }

    public String getStartingAddress2() {
        return startingAddress2;
    }

    public void setStartingAddress2(String startingAddress2) {
        this.startingAddress2 = startingAddress2;
    }

    public String getStartingCity() {
        return startingCity;
    }

    public void setStartingCity(String startingCity) {
        this.startingCity = startingCity;
    }

    public String getStartingState() {
        return startingState;
    }

    public void setStartingState(String startingState) {
        this.startingState = startingState;
    }

    public String getStartingZipcode() {
        return startingZipcode;
    }

    public void setStartingZipcode(String startingZipcode) {
        this.startingZipcode = startingZipcode;
    }

    public String getEndingAddress1() {
        return endingAddress1;
    }

    public void setEndingAddress1(String endingAddress1) {
        this.endingAddress1 = endingAddress1;
    }

    public String getEndingAddress2() {
        return endingAddress2;
    }

    public void setEndingAddress2(String endingAddress2) {
        this.endingAddress2 = endingAddress2;
    }

    public String getEndingCity() {
        return endingCity;
    }

    public void setEndingCity(String endingCity) {
        this.endingCity = endingCity;
    }

    public String getEndingState() {
        return endingState;
    }

    public void setEndingState(String endingState) {
        this.endingState = endingState;
    }

    public String getEndingZipcode() {
        return endingZipcode;
    }

    public void setEndingZipcode(String endingZipcode) {
        this.endingZipcode = endingZipcode;
    }

    public Integer getTripTime() {
        return tripTime;
    }

    public void setTripTime(Integer tripTime) {
        this.tripTime = tripTime;
    }

    public Integer getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(Integer tripDistance) {
        this.tripDistance = tripDistance;
    }

    public Integer getTripCost() {
        return tripCost;
    }

    public void setTripCost(Integer tripCost) {
        this.tripCost = tripCost;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
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

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public void setCarLicensePlate(String carLicensePlate) {
        this.carLicensePlate = carLicensePlate;
    }

    public Integer getCarMpg() {
        return carMpg;
    }

    public void setCarMpg(Integer carMpg) {
        this.carMpg = carMpg;
    }

    public User getDriverId() {
        return driverId;
    }

    public void setDriverId(User driverId) {
        this.driverId = driverId;
    }
    
    
    /*
    ================
    Instance Methods
    ================
     */
    
    /**
     * @return Generates and returns a hash code value for the object with id
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

      /**
     * Checks if the AllRides object identified by 'object' is the same as the AllRides object identified by 'id'
     *
     * @param object The AllRides object identified by 'object'
     * @return True if the AllRides 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AllRides)) {
            return false;
        }
        AllRides other = (AllRides) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
}
