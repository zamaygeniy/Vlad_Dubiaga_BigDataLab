package com.epam.crimes.service;

import com.epam.crimes.entity.ForcesList;
import com.epam.crimes.util.OptionsUtils;
import com.epam.crimes.validator.PropertiesValidator;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Writer {
    public void write(Properties properties) throws InterruptedException, IOException {
        PropertiesValidator propertiesValidator = new PropertiesValidator();
        OptionsUtils optionsUtils = new OptionsUtils();
        if (propertiesValidator.validateProperties(properties)) {
            String fromDate = properties.getProperty("from");
            String toDate = properties.getProperty("to");
            if (fromDate.compareTo(toDate) > 0) {
                String temp = fromDate;
                fromDate = toDate;
                toDate = temp;
            }
            List<String> dates = optionsUtils.getDatesAsList(LocalDate.parse(fromDate + "-01"), LocalDate.parse(toDate + "-01"));
            if ("db".equals(properties.getProperty("write"))) {
                writeToDatabase(properties, dates);
            }
            if ("file".equals(properties.getProperty("write"))) {
                writeToFile(properties, dates);
            }
        }
    }

    private void writeToFile(Properties properties, List<String> dates) throws IOException {

        switch (properties.getProperty("api")) {
            case "stops-force":
                StopService stopService = new StopService();
                String force = properties.getProperty("force");
                stopService.writeAllStopsToFile(force, dates);
                break;
            case "crimes-street":
                CrimeService crimeService = new CrimeService();
                String path = properties.getProperty("path");
                String category = properties.getProperty("category");
                crimeService.writeAllCrimesToFile(path, category, dates);
                break;
        }

    }

    private void writeToDatabase(Properties properties, List<String> dates) throws InterruptedException, IOException {
        switch (properties.getProperty("api")) {
            case "stops-force":
                StopService stopService = new StopService();
                String force = properties.getProperty("force");
                List<String> forces = new ArrayList<>();
                ForcesList forcesList = ForcesList.getInstance();
                if (force.equals("all")) {
                    forces.addAll(forcesList.getForcesId());
                } else {
                    forces.add(force);
                }
                stopService.writeAllStopsToDatabase(forces, dates);
                break;
            case "crimes-street":
                CrimeService crimeService = new CrimeService();
                String path = properties.getProperty("path");
                String category = properties.getProperty("category");
                crimeService.writeAllCrimesToDatabase(Paths.get(path), category, dates);
                break;
        }
    }

}
