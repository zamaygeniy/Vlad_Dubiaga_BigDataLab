package com.epam.crimes.dao.impl;

import com.epam.crimes.connection.DatabaseConnection;
import com.epam.crimes.dao.CrimeDao;
import com.epam.crimes.entity.Crime;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.mapper.ObjectMappers;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import java.io.ObjectStreamClass;
import java.util.List;

public class CrimeDaoImpl extends CrimeDao {



    public CrimeDaoImpl(FluentJdbc fluentJdbc) {
        super(fluentJdbc);
    }

    @Override
    public List<Crime> findAll() {
        Query query = fluentJdbc.query();
        Mapper<Crime> manualCrimeMappper = resultSet -> {
            Crime crime = new Crime();
            crime.setPersistentId(resultSet.getString("persistent_id"));
            crime.setId(resultSet.getInt("id"));
            crime.setLocationType(resultSet.getString("location_type"));
            crime.setCategory(resultSet.getString("category"));
            crime.getLocation().setLatitude(resultSet.getDouble("latitude"));
            crime.getLocation().setLongitude(resultSet.getDouble("longitude"));
            crime.setContext(resultSet.getString("context"));
            crime.setLocationSubtype(resultSet.getString("location_subtype"));
            crime.setContext(resultSet.getString("context"));
            crime.setMonth(resultSet.getDate("month"));
            if (resultSet.getDate("date") != null) {
                crime.getOutcomeStatus().setId(resultSet.getInt("outcome_status_id"));
                crime.getOutcomeStatus().setDate(resultSet.getDate("date"));
                crime.getOutcomeStatus().setCategory(resultSet.getString("outcome_category"));
            } else {
                crime.setOutcomeStatus(null);
            }
            crime.getLocation().getStreet().setId(resultSet.getInt("street_id"));
            crime.getLocation().getStreet().setName(resultSet.getString("name"));
            return crime;
        };
        List<Crime> crimes = query
                .select("SELECT * FROM crimes_schema.crime " +
                        "LEFT JOIN crimes_schema.outcome_status " +
                        "ON crimes_schema.crime.outcome_status_id = crimes_schema.outcome_status.id " +
                        "JOIN crimes_schema.location " +
                        "ON crimes_schema.crime.latitude = crimes_schema.location.latitude " +
                        "AND crimes_schema.crime.longitude = crimes_schema.location.longitude " +
                        "JOIN crimes_schema.street " +
                        "ON crimes_schema.location.street_id = crimes_schema.street.id ")
                .listResult(manualCrimeMappper);
        return crimes;
    }

    @Override
    public void create(List<Crime> crimes) {
        for (Crime crime : crimes) {
            this.create(crime);
        }
    }

    @Override
    public void create(Crime crime) {
        Query query = fluentJdbc.query();
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
}
