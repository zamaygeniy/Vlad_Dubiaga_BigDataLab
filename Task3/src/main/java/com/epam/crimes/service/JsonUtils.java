package com.epam.crimes.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface JsonUtils {

    public <T> List<T> parseUrl(URL url, Class<T[]> entityClass) throws IOException;


}
