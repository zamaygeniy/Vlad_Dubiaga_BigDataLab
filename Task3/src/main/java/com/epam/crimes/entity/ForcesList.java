package com.epam.crimes.entity;

import com.epam.crimes.exception.UrlConnectionException;
import com.epam.crimes.exception.WriterException;
import com.epam.crimes.service.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ForcesList {

    private static final Logger logger = LoggerFactory.getLogger(ForcesList.class);

    private ForcesList() {
        forces = new ArrayList<>();
        try {
            UrlUtils urlUtils = new UrlUtils();
            forces.addAll(urlUtils.parseUrlContent(urlUtils.createUrlForListOfForces(), Force[].class));
        } catch (UrlConnectionException e) {
            logger.error("Failed to create list of forces");
        }
    }

    public static class ForcesListHolder {
        public static final ForcesList INSTANCE = new ForcesList();
    }

    public static ForcesList getInstance() {
        return ForcesListHolder.INSTANCE;
    }

    private final List<Force> forces;

    public List<String> getForcesId(){
        List<String> forcesId = new ArrayList<>();
        forces.stream().forEach(force -> forcesId.add(force.getId()));
        return forcesId;
    }

}
