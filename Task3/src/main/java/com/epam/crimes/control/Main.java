package com.epam.crimes.control;

import com.epam.crimes.service.CrimeService;
import com.epam.crimes.service.impl.CrimeServiceImpl;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ParseException, InterruptedException {
        System.out.println("Start!");
        Options options = new Options();
        Option propertyOption = Option.builder()
                .longOpt("D")
                .argName("property=value")
                .hasArgs()
                .valueSeparator()
                .desc("use value for given properties")
                .build();
        options.addOption(propertyOption);
        options.addOption(Option.builder("h").longOpt("help").desc("show usage information").build());
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h")) {
            //TODO create help message;
            return;
        }

        String path = null;
        String date = null;
        String category = null;

        if (cmd.hasOption("D")) {
            Properties properties = cmd.getOptionProperties("D");
            path = properties.getProperty("path");
            date = properties.getProperty("date");
            if (!date.matches("^((19|20)[0-9]{2})-(0[1-9]|1[012])$")){
                date = null;
            }
            category = properties.getProperty("category");
        }
        if (path != null) {
            CrimeService crimeService = new CrimeServiceImpl();
            crimeService.loadAllCrimesToDataBase(path, category, date);
            System.out.println("Application is running...");
        } else {
            System.err.println("Error. Path is null");
            logger.error("Error. Path is null");
        }


    }
}
