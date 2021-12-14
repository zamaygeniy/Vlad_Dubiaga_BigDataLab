package com.epam.crimes.control;

import com.epam.crimes.service.CrimeService;
import com.epam.crimes.service.impl.CrimeServiceImpl;
import org.apache.commons.cli.*;
import java.util.Properties;


public class Main {

    public static void main(String[] args) throws ParseException, InterruptedException {

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
            path = properties.getProperty("file");
            date = properties.getProperty("date");
            category = properties.getProperty("category");
        }

        CrimeService crimeService = new CrimeServiceImpl();
        crimeService.loadAllCrimesToDataBase(path, category, date);

    }
}
