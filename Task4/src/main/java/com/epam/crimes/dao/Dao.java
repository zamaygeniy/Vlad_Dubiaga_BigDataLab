package com.epam.crimes.dao;

import com.epam.crimes.entity.Entity;
import org.codejargon.fluentjdbc.api.FluentJdbc;

import java.util.List;

public interface Dao<T extends Entity> {

    List<T> findAll();

    void create(T entity);

    void create(List<T> entity);

}
