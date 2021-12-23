package com.epam.crimes.service;

import com.epam.crimes.dao.Dao;
import com.epam.crimes.dao.impl.CrimeDao;
import com.epam.crimes.entity.Crime;
import com.epam.crimes.exception.UrlConnectionException;
import com.epam.crimes.util.FileReader;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class CrimeService {

    public static final int SHUTDOWN_TIME = 5;

    public void create(Crime crime) {
        Dao<Crime> crimeDao = new CrimeDao();
        crimeDao.create(crime);
    }

    public void create(List<Crime> crimeList) {
        if (!crimeList.isEmpty()) {
            Dao<Crime> crimeDao = new CrimeDao();
            crimeDao.create(crimeList);
        }
    }

    public List<Crime> findAll() {
        CrimeDao crimeDao = new CrimeDao();
        return crimeDao.findAll();
    }

    public void writeToDatabaseFromUrl(URL url) throws UrlConnectionException {
        UrlUtils urlUtils = new UrlUtils();
        List<Crime> crimes = urlUtils.parseUrlContent(url, Crime[].class);
        if (!crimes.isEmpty()) {
            create(crimes);
        }
    }

    public void writeAllCrimesToDatabase(Path path, String category, List<String> dates) throws InterruptedException {

        FileReader reader = new FileReader();
        UrlUtils urlUtils = new UrlUtils();
        Queue<List<Double>> coordinatesFromFile = reader.readCoordinatesFromFile(path);

        BlockingQueue<URL> urls = new ArrayBlockingQueue<>(32);
        ConcurrentLinkedQueue<URL> cashedUrls = new ConcurrentLinkedQueue<>();
        ExecutorService crimeLoader = Executors.newFixedThreadPool(16);
        ExecutorService producer = Executors.newSingleThreadExecutor();

        int timeToTermination = SHUTDOWN_TIME * dates.size();

        Runnable produceUrl = () -> coordinatesFromFile.forEach(coordinates -> dates.forEach(date -> {
            URL url = urlUtils.createUrlForCrimesStreet(coordinates, category, date);
            try {
                urls.put(url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        Runnable loadCrime = () -> {
            while (urls.peek() != null) {
                URL url = urls.poll();
                System.out.println(url);
                try {
                    writeToDatabaseFromUrl(url);
                } catch (UrlConnectionException e) {
                    cashedUrls.add(url);
                }
            }
            while (cashedUrls.peek() != null) {
                URL url = cashedUrls.poll();
                try {
                    writeToDatabaseFromUrl(url);
                } catch (UrlConnectionException e) {
                    cashedUrls.add(url);
                }
            }
        };
        startWritingToDatabase(crimeLoader, producer, timeToTermination, produceUrl, loadCrime);
    }

    private void startWritingToDatabase(ExecutorService entityLoader, ExecutorService urlProducer, int timeToTermination, Runnable produceUrl, Runnable loadEntity) throws InterruptedException {
        urlProducer.execute(produceUrl);
        Thread.sleep(5);

        for (int i = 0; i < 16; i++) {
            entityLoader.execute(loadEntity);
        }

        urlProducer.shutdown();
        entityLoader.shutdown();
        try {
            if (!urlProducer.awaitTermination(timeToTermination, TimeUnit.MINUTES)) {
                urlProducer.shutdownNow();
                entityLoader.shutdownNow();
            }
        } catch (InterruptedException e) {
            urlProducer.shutdownNow();
            entityLoader.shutdownNow();
        }
    }
}
