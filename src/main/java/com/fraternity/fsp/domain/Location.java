package com.fraternity.fsp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "nb_street")
    private String nbStreet;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "longitute")
    private String longitute;

    @Column(name = "latitude")
    private String latitude;

    @ManyToOne
    @JsonIgnoreProperties("locations")
    private HelpOffer helpOffer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public Location country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public Location street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNbStreet() {
        return nbStreet;
    }

    public Location nbStreet(String nbStreet) {
        this.nbStreet = nbStreet;
        return this;
    }

    public void setNbStreet(String nbStreet) {
        this.nbStreet = nbStreet;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Location zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLongitute() {
        return longitute;
    }

    public Location longitute(String longitute) {
        this.longitute = longitute;
        return this;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public Location latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public HelpOffer getHelpOffer() {
        return helpOffer;
    }

    public Location helpOffer(HelpOffer helpOffer) {
        this.helpOffer = helpOffer;
        return this;
    }

    public void setHelpOffer(HelpOffer helpOffer) {
        this.helpOffer = helpOffer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", street='" + getStreet() + "'" +
            ", nbStreet='" + getNbStreet() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", longitute='" + getLongitute() + "'" +
            ", latitude='" + getLatitude() + "'" +
            "}";
    }
}
