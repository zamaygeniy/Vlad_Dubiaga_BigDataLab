package com.epam.crimes.entity;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Stop extends Entity {
    private String type;
    @SerializedName("involved_person")
    private Boolean involvedPerson;
    private String datetime;
    private Boolean operation;
    @SerializedName("operation_name")
    private String operationName;
    private Location location;
    private String gender;
    @SerializedName("age_range")
    private String ageRange;
    @SerializedName("self_defined_ethnicity")
    private String selfDefinedEthnicity;
    @SerializedName("officer_defined_ethnicity")
    private String officerDefinedEthnicity;
    private String legislation;
    @SerializedName("object_of_search")
    private String objectOfSearch;
    @SerializedName("outcome_object")
    private OutcomeObject outcomeObject;
    @SerializedName("outcome_linked_to_object_of_search")
    private Boolean outcomeLinkedToObjectOfSearch;
    @SerializedName("removal_of_more_than_outer_clothing")
    private Boolean removalOfMoreThanOuterClothing;


    public Stop(){
        this.setLocation(new Location());
        this.setOutcomeObject(new OutcomeObject());
    }

    @Override
    public String toString() {
        return "Stop{" +
                "type='" + type + '\'' +
                ", involvedPerson=" + involvedPerson +
                ", datetime=" + datetime +
                ", operation=" + operation +
                ", operationName='" + operationName + '\'' +
                ", location=" + location +
                ", gender='" + gender + '\'' +
                ", ageRange='" + ageRange + '\'' +
                ", selfDefinedEthnicity='" + selfDefinedEthnicity + '\'' +
                ", officerDefinedEthnicity='" + officerDefinedEthnicity + '\'' +
                ", legislation='" + legislation + '\'' +
                ", objectOfSearch='" + objectOfSearch + '\'' +
                ", outcomeObject=" + outcomeObject +
                ", outcomeLinkedToObjectOfSearch=" + outcomeLinkedToObjectOfSearch +
                ", removalOfMoreThanOuterClothing=" + removalOfMoreThanOuterClothing +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(type, stop.type) && Objects.equals(involvedPerson, stop.involvedPerson) && Objects.equals(datetime, stop.datetime) && Objects.equals(operation, stop.operation) && Objects.equals(operationName, stop.operationName) && Objects.equals(location, stop.location) && Objects.equals(gender, stop.gender) && Objects.equals(ageRange, stop.ageRange) && Objects.equals(selfDefinedEthnicity, stop.selfDefinedEthnicity) && Objects.equals(officerDefinedEthnicity, stop.officerDefinedEthnicity) && Objects.equals(legislation, stop.legislation) && Objects.equals(objectOfSearch, stop.objectOfSearch) && Objects.equals(outcomeObject, stop.outcomeObject) && Objects.equals(outcomeLinkedToObjectOfSearch, stop.outcomeLinkedToObjectOfSearch) && Objects.equals(removalOfMoreThanOuterClothing, stop.removalOfMoreThanOuterClothing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, involvedPerson, datetime, operation, operationName, location, gender, ageRange, selfDefinedEthnicity, officerDefinedEthnicity, legislation, objectOfSearch, outcomeObject, outcomeLinkedToObjectOfSearch, removalOfMoreThanOuterClothing);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getInvolvedPerson() {
        return involvedPerson;
    }

    public void setInvolvedPerson(Boolean involvedPerson) {
        this.involvedPerson = involvedPerson;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Boolean getOperation() {
        return operation;
    }

    public void setOperation(Boolean operation) {
        this.operation = operation;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getSelfDefinedEthnicity() {
        return selfDefinedEthnicity;
    }

    public void setSelfDefinedEthnicity(String selfDefinedEthnicity) {
        this.selfDefinedEthnicity = selfDefinedEthnicity;
    }

    public String getOfficerDefinedEthnicity() {
        return officerDefinedEthnicity;
    }

    public void setOfficerDefinedEthnicity(String officerDefinedEthnicity) {
        this.officerDefinedEthnicity = officerDefinedEthnicity;
    }

    public String getLegislation() {
        return legislation;
    }

    public void setLegislation(String legislation) {
        this.legislation = legislation;
    }

    public String getObjectOfSearch() {
        return objectOfSearch;
    }

    public void setObjectOfSearch(String objectOfSearch) {
        this.objectOfSearch = objectOfSearch;
    }

    public OutcomeObject getOutcomeObject() {
        return outcomeObject;
    }

    public void setOutcomeObject(OutcomeObject outcomeObject) {
        this.outcomeObject = outcomeObject;
    }

    public Boolean getOutcomeLinkedToObjectOfSearch() {
        return outcomeLinkedToObjectOfSearch;
    }

    public void setOutcomeLinkedToObjectOfSearch(Boolean outcomeLinkedToObjectOfSearch) {
        this.outcomeLinkedToObjectOfSearch = outcomeLinkedToObjectOfSearch;
    }

    public Boolean getRemovalOfMoreThanOuterClothing() {
        return removalOfMoreThanOuterClothing;
    }

    public void setRemovalOfMoreThanOuterClothing(Boolean removalOfMoreThanOuterClothing) {
        this.removalOfMoreThanOuterClothing = removalOfMoreThanOuterClothing;
    }
}
