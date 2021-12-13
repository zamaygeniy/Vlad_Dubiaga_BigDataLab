package com.epam.crimes.dao;

import com.epam.crimes.entity.Entity;
import org.codejargon.fluentjdbc.api.FluentJdbc;

import java.util.List;

public abstract class AbstractDao<T extends Entity> {
    protected FluentJdbc fluentJdbc;

    public abstract List<T> findAll();

    public abstract void create(T entity);

    public abstract void create(List<T> entity);

}
