package com.epam.crimes.service;

import com.epam.crimes.entity.Crime;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface CrimeService {

    void createCrime(Crime crime);

    void createCrime(List<Crime> crimeList);

    List<Crime> findCrimes();

    void loadAllCrimesToDataBase(String path, String category, String date) throws InterruptedException;

    void loadCrimeToDatabase(URL url, String category, String date) throws IOException;

}
