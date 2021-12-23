package com.epam.crimes.validator;

import com.epam.crimes.entity.ForcesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class PropertiesValidator {

    private static final String DATE_REGEX = "^((19|20)[0-9]{2})-(0[1-9]|1[012])$";

    Logger logger = LoggerFactory.getLogger(PropertiesValidator.class);

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
        return forcesList.getForcesId().stream().anyMatch(force::equals);
    }
}
