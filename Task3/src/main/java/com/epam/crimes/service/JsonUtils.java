package com.epam.crimes.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface JsonUtils {

    public <T> List<T> parseUrlContent(URL url, Class<T[]> entityClass) throws IOException;
    public URL createURL(String path, String date);
}
