package com.epam.crimes.service.impl;

import com.epam.crimes.service.JsonUtils;
import com.epam.crimes.service.Reader;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonUtilsImpl implements JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtilsImpl.class);

    private static final String API_METHOD = "https://data.police.uk/api/crimes-street/all-crime";

    @Override
    public <T> List<T> parseUrlContent(URL url, Class<T[]> entityClass) throws IOException {
        {
            if (url == null) {
                logger.error("URL is null");
                return Collections.emptyList();
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (url.toString().length() >= 4094) {
                connection.setRequestMethod("POST");
            } else {
                connection.setRequestMethod("GET");
            }

            connection.connect();
            int code = connection.getResponseCode();
            if (code >= 400) {
                logger.error("Connection error. Code: {}. URL: {}", code, url);
                return Collections.emptyList();
            }

            T[] entity = null;
            try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
                String content = IOUtils.toString(in, StandardCharsets.UTF_8);
                entity = new GsonBuilder().setDateFormat("yyyy-MM").create().fromJson(content, entityClass);
            }
            if (entity != null) {
                return Arrays.asList(entity);
            } else {
                logger.error("No valid data from: {}", url);
                return Collections.emptyList();
            }
        }
    }

    @Override
    public URL createURL(String path, String date) {
        Reader reader = new ReaderImpl();

        List<Double> coordinates = reader.readCoordinatesFromFile(path);
        if (coordinates.size() % 2 != 0) {
            coordinates.remove(coordinates.size() - 1);
        }

        StringBuilder url = new StringBuilder(API_METHOD);
        if (coordinates.size() > 2) {
            url.append("?poly=");
            for (int i = 0; i < coordinates.size(); i += 2) {
                url.append(coordinates.get(i)).append(",").append(coordinates.get(i + 1)).append(":");
            }
            url.deleteCharAt(url.length() - 1);
        } else {
            url.append("?lat=").append(coordinates.get(0)).append("&lng=").append(coordinates.get(1));
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
