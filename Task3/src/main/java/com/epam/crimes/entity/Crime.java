package com.epam.crimes.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;

public class Crime {
    private String category;
    private Location location;
    @SerializedName("location_type")
    private String locationType;
    @SerializedName("location_subtype")
    private String locationSubtype;
    private int id;
    @SerializedName("persistent_id")
    private String persistentId;
    @SerializedName("outcome_status")
    private OutcomeStatus outcomeStatus;
    private String context;
    private Date month;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;
        return id == crime.id && Objects.equals(category, crime.category) && Objects.equals(location, crime.location) && Objects.equals(locationType, crime.locationType) && Objects.equals(locationSubtype, crime.locationSubtype) && Objects.equals(persistentId, crime.persistentId) && Objects.equals(outcomeStatus, crime.outcomeStatus) && Objects.equals(context, crime.context) && Objects.equals(month, crime.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, location, locationType, locationSubtype, id, persistentId, outcomeStatus, context, month);
    }

    @Override
    public String toString() {
        return "Crime{" +
                "category='" + category + '\'' +
                ", location=" + location +
                ", locationType='" + locationType + '\'' +
                ", locationSubtype='" + locationSubtype + '\'' +
                ", id=" + id +
                ", persistentId='" + persistentId + '\'' +
                ", outcomeStatus=" + outcomeStatus +
                ", context='" + context + '\'' +
                ", month=" + month +
                '}';
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationSubtype() {
        return locationSubtype;
    }

    public void setLocationSubtype(String locationSubtype) {
        this.locationSubtype = locationSubtype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public OutcomeStatus getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(OutcomeStatus outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

}
