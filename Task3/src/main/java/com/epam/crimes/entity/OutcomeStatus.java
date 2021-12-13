package com.epam.crimes.entity;

import java.util.Date;
import java.util.Objects;

public class OutcomeStatus {
    private int id;
    private String category;
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutcomeStatus that = (OutcomeStatus) o;
        return id == that.id && Objects.equals(category, that.category) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, date);
    }

    @Override
    public String toString() {
        return "OutcomeStatus{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", date=" + date +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
