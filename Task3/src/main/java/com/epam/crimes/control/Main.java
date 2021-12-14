package com.epam.crimes.control;

import com.epam.crimes.entity.Crime;
import com.epam.crimes.connection.DatabaseConnection;
import com.epam.crimes.service.CrimeService;
import com.epam.crimes.service.JsonUtils;
import com.epam.crimes.service.Reader;
import com.epam.crimes.service.impl.CrimeServiceImpl;
import com.epam.crimes.service.impl.JsonUtilsImpl;
import com.epam.crimes.service.impl.ReaderImpl;
import com.zaxxer.hikari.HikariDataSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        JsonUtils jsonUtils = new JsonUtilsImpl();
        URL url = jsonUtils.createURL(path, date);
        List<Crime> crimes = jsonUtils.parseUrlContent(url, Crime[].class);
        CrimeService crimeService = new CrimeServiceImpl();
        crimeService.createCrime(crimes);
    }
}
