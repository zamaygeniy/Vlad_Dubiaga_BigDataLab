package com.epam.crimes.util;

import com.epam.crimes.exception.UrlConnectionException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UrlUtils {

    private static final Logger logger = LoggerFactory.getLogger(UrlUtils.class);

    private static final String API_METHOD = "https://data.police.uk/api/";

    public String getJsonFromUrl(URL url) throws UrlConnectionException {
        if (url == null) {
            logger.error("URL is null");
            return "";
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (url.toString().length() >= 4094) {
                connection.setRequestMethod("POST");
            } else {
                connection.setRequestMethod("GET");
            }

            connection.connect();

            if (connection.getResponseCode() == 429 || connection.getResponseCode() == 500) {
                logger.info("Connection error. Code: {}. URL: {}", connection.getResponseCode(), url);
                throw new UrlConnectionException("Connection error. Code: " + connection.getResponseCode() + ". URL: " + url);
            }

            if (connection.getResponseCode() >= 400) {
                if (connection.getResponseCode() == 503) {
                    logger.error("Error. Custom area contains more than 10,000 crimes. Code: {}. URL: {}", connection.getResponseCode(), url);
                } else {
                    logger.error("Connection error. Code: {}. URL: {}", connection.getResponseCode(), url);
                }
                return "";
            }

            try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
                return IOUtils.toString(in, StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            logger.error("Connection error. URL: {}", url);
            return "";
        }
    }

    public URL createUrlForStopsForce(String force, String date) {
        StringBuilder url = new StringBuilder(API_METHOD + "stops-force");
        url.append("?force=").append(force).append("&date=").append(date);
        try {
            return new URL(url.toString());
        } catch (MalformedURLException e) {
            logger.error("Failed to create URL: {}", url);
            e.printStackTrace();
            return null;
        }
    }

    public URL createUrlForListOfForces(){
        StringBuilder url = new StringBuilder(API_METHOD).append("forces");
        try {
            return new URL(url.toString());
        } catch (MalformedURLException e){
            logger.error("Failed to create URL: {}", url);
            e.printStackTrace();
            return null;
        }
    }

    public URL createUrlForCrimesStreet(List<Double> coordinates, String category, String date) {
        StringBuilder url = new StringBuilder(API_METHOD + "crimes-street/");

        url.append(Objects.requireNonNullElse(category, "all-crime"));

        if (!coordinates.isEmpty()) {
            if (coordinates.size() % 2 != 0) {
                coordinates.remove(coordinates.size() - 1);
            }

            if (coordinates.size() > 2) {
                url.append("?poly=");
                for (int i = 0; i < coordinates.size(); i += 2) {
                    url.append(coordinates.get(i)).append(",").append(coordinates.get(i + 1)).append(":");
                }
                url.deleteCharAt(url.length() - 1);
            } else {
                url.append("?lng=").append(coordinates.get(0)).append("&lat=").append(coordinates.get(1));
            }
        }

        if (date != null) {
            url.append("&date=").append(date);
        }

        try {
            return new URL(url.toString());
        } catch (MalformedURLException e) {
            logger.error("Failed to create URL: {}", url);
            e.printStackTrace();
            return null;
        }

    }

}
