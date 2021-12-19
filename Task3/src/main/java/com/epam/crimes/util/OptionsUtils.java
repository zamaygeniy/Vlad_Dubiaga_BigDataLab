package com.epam.crimes.util;

import org.apache.commons.cli.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OptionsUtils {
    public CommandLine getCommandLine(String[] args) throws ParseException {
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
        return parser.parse(options, args);
    }

    public List<String> getDatesAsList(LocalDate fromDate, LocalDate toDate) {
        List<String> dateList = new ArrayList<>();
        for (LocalDate date = fromDate; date.isBefore(toDate) || date.equals(toDate); date = date.plusMonths(1)) {
            dateList.add(date.getYear() + "-" + date.getMonthValue());
        }
        return dateList;
    }

}
