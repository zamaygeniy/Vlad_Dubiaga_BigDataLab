package com.epam.crimes.service;

import com.epam.crimes.entity.Crime;

import java.util.List;

public interface CrimeService {

    void createCrime(Crime crime);
    void createCrime(List<Crime> crimeList);
    List<Crime> findCrimes();

}
