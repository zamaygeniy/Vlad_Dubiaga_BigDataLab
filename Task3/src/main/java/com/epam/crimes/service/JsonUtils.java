package com.epam.crimes.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface JsonUtils {

    <T> List<T> parseUrlContent(URL url, Class<T[]> entityClass) throws IOException;

    URL createURL(List<Double> coordinates, String category, String date);

}
