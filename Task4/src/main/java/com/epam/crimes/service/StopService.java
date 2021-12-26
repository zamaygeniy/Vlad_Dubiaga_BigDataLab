package com.epam.crimes.service;

import com.epam.crimes.dao.Dao;
import com.epam.crimes.dao.impl.StopDao;
import com.epam.crimes.entity.ForcesList;
import com.epam.crimes.entity.Stop;
import com.epam.crimes.exception.UrlConnectionException;
import com.epam.crimes.util.UrlUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

//спросить про интерфейс
public class StopService {

    public static final int SHUTDOWN_TIME = 2;

    public void create(Stop stop) {
        Dao<Stop> stopDao = new StopDao();
        stopDao.create(stop);
    }

    public void create(List<Stop> stopList) {
        if (!stopList.isEmpty()) {
            Dao<Stop> stopDao = new StopDao();
            stopDao.create(stopList);
        }
    }

    public void writeToDatabaseFromUrl(URL url) throws UrlConnectionException {
        UrlUtils urlUtils = new UrlUtils();
        ApiContentProvider apiContentProvider = new ApiContentProvider();
        List<Stop> stops = apiContentProvider.parseJsonContent(urlUtils.getJsonFromUrl(url), Stop[].class);
        if (!stops.isEmpty()) {
            create(stops);
        }
    }

    public void writeAllStopsToDatabase(List<String> forces, List<String> dates) throws InterruptedException {
        UrlUtils urlUtils = new UrlUtils();
        BlockingQueue<URL> urls = new ArrayBlockingQueue<>(16);
        ConcurrentLinkedQueue<URL> cashedUrls = new ConcurrentLinkedQueue<>();
        ExecutorService crimeLoader = Executors.newFixedThreadPool(8);
        ExecutorService urlProducer = Executors.newSingleThreadExecutor(); //спросить стоило ли разделять

        int timeToTermination = SHUTDOWN_TIME * forces.size() * dates.size();

        Runnable produceUrl = () -> forces.forEach(force -> dates.forEach(date -> {
            URL url = urlUtils.createUrlForStopsForce(force, date);
            try {
                urls.put(url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        Runnable writeStop = () -> {
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
        startWriting(crimeLoader, urlProducer, timeToTermination, produceUrl, writeStop);
    }

    private void startWriting(ExecutorService entityLoader, ExecutorService urlProducer, int timeToTermination, Runnable produceUrl, Runnable writeEntity) throws InterruptedException {
        urlProducer.execute(produceUrl);
        Thread.sleep(5);

        for (int i = 0; i < 8; i++) {
            entityLoader.execute(writeEntity);
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

    public void writeAllStopsToFile(String force, List<String> dates) throws IOException {
        UrlUtils urlUtils = new UrlUtils();
        Queue<URL> urls = new ArrayDeque<>();
        addStopUrlToQueue(force, dates, urls);
        try (FileWriter writer = new FileWriter("data.txt", true)) {
            while (urls.peek() != null) {
                URL url = urls.poll();
                try {
                    writer.append(urlUtils.getJsonFromUrl(url)); //спросить стоить ли постоянно открывать файл или держать открытым
                } catch (UrlConnectionException e) {
                    urls.add(url);
                }
            }
        }
    }

    private void addStopUrlToQueue(String force, List<String> dates, Queue<URL> urls) {
        UrlUtils urlUtils = new UrlUtils();
        ForcesList forcesList = ForcesList.getInstance();
        if (force.equals("all")) {
            forcesList.getForcesId().forEach(forceId -> dates.forEach(date -> urls.add(urlUtils.createUrlForStopsForce(forceId, date))));
        } else {
            dates.forEach(date -> urls.add(urlUtils.createUrlForStopsForce(force, date)));
        }
    }


}
