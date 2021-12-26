package com.epam.crimes.validator;

import com.epam.crimes.entity.ForcesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesValidator {

    private static final String DATE_REGEX = "^((19|20)[0-9]{2})-(0[1-9]|1[012])$";
    private static final String API_REGEX = "stops-force|crimes-street";
    private static final String WRITE_REGEX = "db|file";

    Logger logger = LoggerFactory.getLogger(PropertiesValidator.class);

    public boolean validateProperties(Properties properties){
        String api = properties.getProperty("api");
        String path = properties.getProperty("path");
        String from = properties.getProperty("from");
        String to = properties.getProperty("to");
        String write = properties.getProperty("write");
        String force = properties .getProperty("force");
        if (api.equals("stops-force")){
            return validateDate(from) && validateDate(to) && validateWrite(write) && validateForce(force);
        }
        if (api.equals("crimes-street")){
            return validateDate(from) && validateDate(to) && validateWrite(write) && validatePath(path);
        }
        return false;
    }

    public boolean validateWrite(String write){
        return write != null && write.matches(WRITE_REGEX);
    }

    public boolean validateApi(String api){
        return api != null && api.matches(API_REGEX);
    }

    public boolean validatePath(String filePath) {
        try {
            Paths.get(filePath);
            return true;
        } catch (InvalidPathException e) {
            logger.error("Invalid path. Path : {}", filePath);
        }
        return false;
    }

    public boolean validateDate(String date) {
        return date != null && date.matches(DATE_REGEX);
    }

    public boolean validateForce(String force) {
        ForcesList forcesList = ForcesList.getInstance();
        return force.equals("all") || forcesList.getForcesId().stream().anyMatch(force::equals);
    }
}
