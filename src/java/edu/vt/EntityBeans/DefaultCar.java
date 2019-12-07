/*
 * Created by Justin Kennedy on 2019.12.06  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.EntityBeans;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jusmk96
 */
@Entity
@Table(name = "DefaultCar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DefaultCar.findAll", query = "SELECT d FROM DefaultCar d")
    , @NamedQuery(name = "DefaultCar.findById", query = "SELECT d FROM DefaultCar d WHERE d.id = :id")
    , @NamedQuery(name = "DefaultCar.findByMake", query = "SELECT d FROM DefaultCar d WHERE d.make = :make")
    , @NamedQuery(name = "DefaultCar.findByModel", query = "SELECT d FROM DefaultCar d WHERE d.model = :model")
    , @NamedQuery(name = "DefaultCar.findByColor", query = "SELECT d FROM DefaultCar d WHERE d.color = :color")
    , @NamedQuery(name = "DefaultCar.findByLicensePlate", query = "SELECT d FROM DefaultCar d WHERE d.licensePlate = :licensePlate")
    , @NamedQuery(name = "DefaultCar.findByMpg", query = "SELECT d FROM DefaultCar d WHERE d.mpg = :mpg")})
public class DefaultCar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "make")
    private String make;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "model")
    private String model;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "color")
    private String color;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "licensePlate")
    private String licensePlate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mpg")
    private int mpg;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public DefaultCar() {
    }

    public DefaultCar(Integer id) {
        this.id = id;
    }

    public DefaultCar(Integer id, String make, String model, String color, String licensePlate, int mpg) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.color = color;
        this.licensePlate = licensePlate;
        this.mpg = mpg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getMpg() {
        return mpg;
    }

    public void setMpg(int mpg) {
        this.mpg = mpg;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefaultCar)) {
            return false;
        }
        DefaultCar other = (DefaultCar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.vt.EntityBeans.DefaultCar[ id=" + id + " ]";
    }
    
}
