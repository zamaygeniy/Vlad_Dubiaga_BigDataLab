package com.epam.crimes.service;

import com.epam.crimes.entity.ForcesList;
import com.epam.crimes.exception.UrlConnectionException;
import com.epam.crimes.exception.WriterException;
import com.epam.crimes.util.FileReader;
import com.epam.crimes.util.OptionsUtils;
import com.epam.crimes.validator.PropertiesValidator;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Writer {
    public void write(Properties properties) throws InterruptedException, WriterException {
        PropertiesValidator propertiesValidator = new PropertiesValidator();
        OptionsUtils optionsUtils = new OptionsUtils();
        String fromDate = properties.getProperty("from");
        String toDate = properties.getProperty("to");
        UrlUtils urlUtils = new UrlUtils();

        if (propertiesValidator.validateDate(fromDate) && propertiesValidator.validateDate(toDate)) {
            if (fromDate.compareTo(toDate) > 0) {
                String temp = fromDate;
                fromDate = toDate;
                toDate = temp;
            }
            List<String> dates = optionsUtils.getDatesAsList(LocalDate.parse(fromDate + "-01"), LocalDate.parse(toDate + "-01"));

            if (properties.get("write") == "db") {
                writeToDatabase(properties, dates);
            }
            if (properties.get("write") == "file") {
                writeToFile(properties, dates);
            }
        }
    }

    private void writeToFile(Properties properties, List<String> dates) throws WriterException {
        Queue<URL> urls = new ArrayDeque<>();
        UrlUtils urlUtils = new UrlUtils();
        switch (properties.getProperty("api")) {
            case "stops-force":
                String force = properties.getProperty("force");
                addStopUrlToQueue(force, dates, urls);
                break;
            case "crimes-street":
                String path = properties.getProperty("path");
                String category = properties.getProperty("category");
                addCrimeUrlToQueue(path, category, dates, urls);
                break;
        }
        try (FileWriter writer = new FileWriter("data.txt", true)) {
            while (urls.peek() != null) {
                URL url = urls.poll();
                try {
                    writer.append(urlUtils.getJsonFromUrl(url)); //спросить стоить ли постоянно открывать файл или держать открытым
                } catch (UrlConnectionException e) {
                    urls.add(url);
                }
            }
        } catch (IOException e){
            throw new WriterException(e);
        }


    }

    private void writeToDatabase(Properties properties, List<String> dates) throws InterruptedException {
        PropertiesValidator propertiesValidator = new PropertiesValidator();
        switch (properties.getProperty("api")) {
            case "stops-force":
                StopService stopService = new StopService();
                String force = properties.getProperty("force");
                List<String> forces = new ArrayList<>();
                ForcesList forcesList = ForcesList.getInstance();
                if (force.equals("all")) {
                    forces.addAll(forcesList.getForcesId());
                } else {
                    if (propertiesValidator.validateForce(force)) {
                        forces.add(force);
                    }
                }
                stopService.writeAllStopsToDatabase(forces, dates);
                break;
            case "crimes-street":
                CrimeService crimeService = new CrimeService();
                String path = properties.getProperty("path");
                String category = properties.getProperty("category");
                if (propertiesValidator.validatePath(path)) {
                    crimeService.writeAllCrimesToDatabase(Paths.get(path), category, dates);
                }
                break;
        }
    }


    private void addCrimeUrlToQueue(String path, String category, List<String> dates, Queue<URL> urls) {
        UrlUtils urlUtils = new UrlUtils();
        PropertiesValidator propertiesValidator = new PropertiesValidator();
        if (propertiesValidator.validatePath(path)) {
            FileReader fileReader = new FileReader();
            Queue<List<Double>> coordinatesFromFile = fileReader.readCoordinatesFromFile(Paths.get(path));
            coordinatesFromFile.forEach(coordinates -> dates.forEach(date -> urls.add(urlUtils.createUrlForCrimesStreet(coordinates, category, date))));
        }
    }

    private void addStopUrlToQueue(String force, List<String> dates, Queue<URL> urls) {
        UrlUtils urlUtils = new UrlUtils();
        PropertiesValidator propertiesValidator = new PropertiesValidator();
        ForcesList forcesList = ForcesList.getInstance();
        if (force.equals("all")) {
            forcesList.getForcesId().forEach(forceId -> dates.forEach(date -> urls.add(urlUtils.createUrlForStopsForce(forceId, date))));
        } else {
            if (propertiesValidator.validateForce(force)) {
                dates.forEach(date -> urls.add(urlUtils.createUrlForStopsForce(force, date)));
            }
        }
    }
}
