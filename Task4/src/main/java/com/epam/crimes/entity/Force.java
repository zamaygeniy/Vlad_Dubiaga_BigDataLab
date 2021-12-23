package com.epam.crimes.entity;

import java.util.Objects;

public class Force extends Entity {
    private String id;
    private String name;

    public Force(String id, String name){
        this.setId(id);
        this.setName(name);
    }

    @Override
    public String toString() {
        return "Forces{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Force force = (Force) o;
        return Objects.equals(id, force.id) && Objects.equals(name, force.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
