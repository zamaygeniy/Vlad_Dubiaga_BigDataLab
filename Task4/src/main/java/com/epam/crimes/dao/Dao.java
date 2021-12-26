package com.epam.crimes.dao;

import com.epam.crimes.entity.Entity;

import java.util.List;

public interface Dao<T extends Entity> {

    void create(T entity);

    void create(List<T> entity);

}
