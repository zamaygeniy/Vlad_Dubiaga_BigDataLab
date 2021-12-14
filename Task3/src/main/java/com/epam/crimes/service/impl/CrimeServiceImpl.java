package com.epam.crimes.service.impl;

import com.epam.crimes.connection.DatabaseConnection;
import com.epam.crimes.dao.CrimeDao;
import com.epam.crimes.dao.impl.CrimeDaoImpl;
import com.epam.crimes.entity.Crime;
import com.epam.crimes.service.CrimeService;
import com.epam.crimes.service.JsonUtils;
import com.epam.crimes.service.Reader;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CrimeServiceImpl implements CrimeService {

    @Override
    public void loadAllCrimesToDataBase(String path, String category, String date) {
        Reader reader = new ReaderImpl();
        JsonUtils jsonUtils = new JsonUtilsImpl();

        ConcurrentLinkedQueue<List<Double>> coordinatesFromFile = reader.readCoordinatesFromFile(path);
        ConcurrentLinkedQueue<URL> urls = coordinatesFromFile.stream()
                .map(coordinates -> jsonUtils.createURL(coordinates, category, date))
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));

        Runnable loadCrime = () -> {
            while (urls.peek() != null) {
                URL url = urls.poll();
                try {
                    loadCrimeToDatabase(url, category, date);
                } catch (ConnectException e) {
                    urls.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(loadCrime);
        }
        executorService.shutdown();
    }

    @Override
    public void loadCrimeToDatabase(URL url, String category, String date) throws IOException {
        JsonUtils jsonUtils = new JsonUtilsImpl();
        List<Crime> crimes = jsonUtils.parseUrlContent(url, Crime[].class);
        createCrime(crimes);
    }

    @Override
    public void createCrime(Crime crime) {
        CrimeDao crimeDao = new CrimeDaoImpl(DatabaseConnection.getInstance().getFluentJdbc());
        crimeDao.create(crime);
    }

    @Override
    public void createCrime(List<Crime> crimeList) {
        if (!crimeList.isEmpty()) {
            CrimeDao crimeDao = new CrimeDaoImpl(DatabaseConnection.getInstance().getFluentJdbc());
            crimeDao.create(crimeList);
        }
    }

    @Override
    public List<Crime> findCrimes() {
        CrimeDao crimeDao = new CrimeDaoImpl(DatabaseConnection.getInstance().getFluentJdbc());
        return crimeDao.findAll();
    }
}
