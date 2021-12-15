package com.epam.crimes.dao;

import com.epam.crimes.entity.Crime;
import org.codejargon.fluentjdbc.api.FluentJdbc;

public abstract class CrimeDao extends AbstractDao<Crime>{

    public CrimeDao(FluentJdbc fluentJdbc) {
        super.fluentJdbc = fluentJdbc;

    }
}
