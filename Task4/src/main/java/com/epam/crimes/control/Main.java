package com.epam.crimes.control;

import com.epam.crimes.exception.WriterException;
import com.epam.crimes.service.Writer;
import com.epam.crimes.util.OptionsUtils;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class Main {

    public static void main(String[] args) throws ParseException, InterruptedException, WriterException {
        OptionsUtils optionsUtils = new OptionsUtils();
        CommandLine commandLine = optionsUtils.getCommandLine(args);
        if (commandLine.hasOption("h")){
            System.out.println("HELP MESSAGE!");
            System.exit(0);
        }
        System.out.println("Start!");
        if (commandLine.hasOption("D")){
            Properties properties = commandLine.getOptionProperties("D");
            Writer writer = new Writer();
            writer.write(properties);
        }


    }
}
