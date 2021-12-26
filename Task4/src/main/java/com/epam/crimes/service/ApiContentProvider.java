package com.epam.crimes.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiContentProvider {

    private static final Logger logger = LoggerFactory.getLogger(ApiContentProvider.class);

    public <T> List<T> parseJsonContent(String content, Class<T[]> entityClass){

        if (content == null) {
            logger.error("Content is null");
            return Collections.emptyList();
        }

        T[] entity;
        try {
            entity = new GsonBuilder().setDateFormat("yyyy-MM").create().fromJson(content, entityClass);
            if (entity != null) {
                return Arrays.asList(entity);
            }
        } catch (JsonSyntaxException e){
            logger.error("Json parsing error");
        }
        return Collections.emptyList();

    }
}
