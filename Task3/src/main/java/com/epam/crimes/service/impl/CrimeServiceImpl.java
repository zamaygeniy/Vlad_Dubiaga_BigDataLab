package com.epam.crimes.service.impl;

import com.epam.crimes.connection.DatabaseConnection;
import com.epam.crimes.dao.CrimeDao;
import com.epam.crimes.dao.impl.CrimeDaoImpl;
import com.epam.crimes.entity.Crime;
import com.epam.crimes.service.CrimeService;

import java.util.List;

public class CrimeServiceImpl implements CrimeService {
    @Override
    public void createCrime(Crime crime) {
        CrimeDao crimeDao = new CrimeDaoImpl(DatabaseConnection.getInstance().getFluentJdbc());
        crimeDao.create(crime);
    }

    @Override
    public void createCrime(List<Crime> crimeList) {
        CrimeDao crimeDao = new CrimeDaoImpl(DatabaseConnection.getInstance().getFluentJdbc());
        crimeDao.create(crimeList);
    }

    @Override
    public List<Crime> findCrimes(){
        CrimeDao crimeDao = new CrimeDaoImpl(DatabaseConnection.getInstance().getFluentJdbc());
        return crimeDao.findAll();
    }
}
