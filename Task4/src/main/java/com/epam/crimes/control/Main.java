package com.epam.crimes.control;

import com.epam.crimes.service.Writer;
import com.epam.crimes.util.OptionsUtils;
import org.apache.commons.cli.*;
import java.io.IOException;
import java.util.Properties;


public class Main {

    public static void main(String[] args) throws ParseException, InterruptedException, IOException {
        OptionsUtils optionsUtils = new OptionsUtils();
        CommandLine commandLine = optionsUtils.getCommandLine(args);
        System.out.println("Start!");
        if (commandLine.hasOption("D")){
            Properties properties = commandLine.getOptionProperties("D");
            Writer writer = new Writer();
            writer.write(properties);
        }
    }
}
