/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.model;

import com.barmagli.helper.Helper;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author MohamedGamal
 */
public class Fax implements Formatable {

    public int rank;

    // Basic Properties 
    private int id;
    private String about;
    private Date date;
    private int sendNumber;
    private String savedNumber;
    private boolean state;
    private boolean type;
    private List<FaxPlace> faxPlaces;
    private List<FaxImage> faxImages;
    // Relation Properties
    private int faxRef;

    public Fax() {
    }

    public Fax(String about, Date date, int sendNumber, String savedNumber, boolean state, boolean type, List<FaxPlace> faxPlaces, List<FaxImage> faxImages, int faxRef) {
        this.about = about;
        this.date = date;
        this.sendNumber = sendNumber;
        this.savedNumber = savedNumber;
        this.state = state;
        this.type = type;
        this.faxPlaces = faxPlaces;
        this.faxImages = faxImages;
        this.faxRef = faxRef;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fax other = (Fax) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fax{" + "id=" + id + ", about=" + about + ", date=" + date + ", sendNumber=" + sendNumber + ", savedNumber=" + savedNumber + ", state=" + state + ", type=" + type + ", faxPlaces=" + faxPlaces + ", faxImages=" + faxImages + ", faxRef=" + faxRef + '}';
    }

    public List<FaxImage> getFaxImages() {
        return faxImages;
    }

    public void setFaxImages(List<FaxImage> faxImages) {
        this.faxImages = faxImages;
    }

    public List<FaxPlace> getFaxPlaces() {
        return faxPlaces;
    }

    public void setFaxPlaces(List<FaxPlace> faxPlaces) {
        this.faxPlaces = faxPlaces;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getSavedNumber() {
        return savedNumber;
    }

    public void setSavedNumber(String savedNumber) {
        this.savedNumber = savedNumber;
    }

    public int getFaxRef() {
        return faxRef;
    }

    public void setFaxRef(int faxRef) {
        this.faxRef = faxRef;
    }

    @Override
    public Object[] formateToTable() {
        String stringPlaces = "";
        for (FaxPlace faxPlace : faxPlaces) {
            stringPlaces += faxPlace.getName() + " - ";
        }
        stringPlaces = Helper.rTrim(stringPlaces, " - ");
        String faxData[] = new String[]{
            "" + this.id,
            this.about,
            stringPlaces,
            (this.type) ? "فاكس" : "إشارة",
            "" + this.sendNumber,
            "" + DatabaseFaxOperation.getSendNumberFormFaxRef(faxRef),
            (this.state) ? "صادر" : "وارد"
        };
        return faxData;
    }

}
