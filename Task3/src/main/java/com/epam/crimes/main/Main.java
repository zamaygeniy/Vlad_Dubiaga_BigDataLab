package com.epam.crimes.main;

import com.epam.crimes.entity.Crime;
import com.epam.crimes.util.JsonUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResult;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String FILE_URL = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2021-05";
    private static final String FILE_NAME = "D:\\Vlad_Dubiaga_BigDataLab\\Task3\\test.txt";

    public static void main(String[] args) throws IOException {


        URL url = new URL(FILE_URL);
        List<Crime> crimes = JsonUtils.parseUrl(url);
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/crimes");
        config.setUsername("postgres");
        config.setPassword("reverse");
        config.setSchema("crimes_schema");
        HikariDataSource dataSource = new HikariDataSource(config);
        FluentJdbc fluentJdbc = new FluentJdbcBuilder()
                .connectionProvider(dataSource)
                .build();

        Query query = fluentJdbc.query();

        for (Crime crime : crimes) {
            query.transaction().in(
                    () -> {
                        query
                                .update("INSERT INTO crimes_schema.street(name, id) VALUES (?, ?) ON CONFLICT DO NOTHING")
                                .params(crime.getLocation().getStreet().getName(), crime.getLocation().getStreet().getId())
                                .run();
                        query
                                .update("INSERT INTO crimes_schema.location(latitude, longitude, street_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING")
                                .params(crime.getLocation().getLatitude(), crime.getLocation().getLongitude(), crime.getLocation().getStreet().getId())
                                .run();

                        Integer outcomeStatusId = null;
                        if (crime.getOutcomeStatus() != null) {
                            UpdateResultGenKeys<Integer> result = query
                                    .update("INSERT INTO crimes_schema.outcome_status(date, category) VALUES (?, ?)")
                                    .params(crime.getOutcomeStatus().getDate(), crime.getOutcomeStatus().getCategory())
                                    .runFetchGenKeys(Mappers.singleInteger());
                            outcomeStatusId = result.generatedKeys().get(0);
                            crime.getOutcomeStatus().setId(outcomeStatusId);
                        }

                        query
                                .update("INSERT INTO crimes_schema.crime(persistent_id, id, location_type, category, latitude, longitude, context, location_subtype, outcome_status, month) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING")
                                .params(crime.getPersistentId(), crime.getId(), crime.getLocationType(), crime.getCategory(), crime.getLocation().getLatitude(), crime.getLocation().getLongitude(), crime.getContext(), crime.getLocationSubtype(), outcomeStatusId, crime.getMonth())
                                .run();
                        return null;
                    }
            );
        }
        /*try {
            File file = new File("D:\\Vlad_Dubiaga_BigDataLab\\Task3\\LondonStations.csv");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            -?\d+\.?\d+
            while (line != null) {
                line = reader.readLine();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/





    }

}
