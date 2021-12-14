package com.epam.crimes.control;

import com.epam.crimes.entity.Crime;
import com.epam.crimes.service.CrimeService;
import com.epam.crimes.service.JsonUtils;
import com.epam.crimes.service.impl.CrimeServiceImpl;
import com.epam.crimes.service.impl.JsonUtilsImpl;
import org.apache.commons.cli.*;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
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

        if (cmd.hasOption("h")){
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

        JsonUtils jsonUtils = new JsonUtilsImpl();
        URL url = jsonUtils.createURL(path, category, date);
        List<Crime> crimes = jsonUtils.parseUrlContent(url, Crime[].class);
        CrimeService crimeService = new CrimeServiceImpl();
        crimeService.createCrime(crimes);
    }
}
