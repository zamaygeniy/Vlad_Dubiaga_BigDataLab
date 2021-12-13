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
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final String FILE_URL = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2017-01";
    private static final String FILE_NAME = "D:\\Vlad_Dubiaga_BigDataLab\\Task3\\test.txt";

    public static void main(String[] args) throws IOException {

            /*Reader reader = new ReaderImpl();
            //List<Double> list = reader.readFromFile("LondonStations.csv");
            URL url = new URL(FILE_URL);
            JsonUtils jsonUtils = new JsonUtilsImpl();
            List<Crime> crimes = jsonUtils.parseUrl(url, Crime[].class);

            HikariDataSource dataSource = DatabaseConnection.getInstance().getDataSource();

            FluentJdbc fluentJdbc = new FluentJdbcBuilder()
                    .connectionProvider(dataSource)
                    .build();*/

        CrimeService crimeService = new CrimeServiceImpl();
        List<Crime> list = crimeService.findCrimes();
        System.out.println(list.size());
        HashSet<Crime> set = new HashSet<>(list);
        System.out.println(set.size());





    }

}
