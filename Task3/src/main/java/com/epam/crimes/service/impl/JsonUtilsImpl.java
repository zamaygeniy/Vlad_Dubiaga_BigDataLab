package com.epam.crimes.service.impl;

import com.epam.crimes.service.JsonUtils;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonUtilsImpl implements JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtilsImpl.class);

    @Override
    public <T> List<T> parseUrl(URL url, Class<T[]> entityClass) throws IOException {
        {
            String content = "";
            if (url == null) {
                logger.error("URL is null");
                return Collections.emptyList();
            }
            T[] entity = null;
            try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {
                content = IOUtils.toString(in, StandardCharsets.UTF_8);
                entity = new GsonBuilder().setDateFormat("yyyy-MM").create().fromJson(content, entityClass);
            }
            if (entity != null) {
                return Arrays.asList(entity);
            } else {
                logger.error("No valid data in file: {}", url);
                return Collections.emptyList();
            }
        }
    }
}
