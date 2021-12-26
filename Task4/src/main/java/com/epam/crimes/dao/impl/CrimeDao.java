package com.epam.crimes.dao.impl;


import com.epam.crimes.connection.DatabaseConnection;
import com.epam.crimes.dao.Dao;
import com.epam.crimes.entity.Crime;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import java.util.List;

public class CrimeDao implements Dao<Crime> {

    private FluentJdbc fluentJdbc = DatabaseConnection.getInstance().getFluentJdbc();

    private static final String INSERT_CRIME =
            "INSERT INTO crimes_schema.crime(persistent_id, id, location_type, category, context, location_subtype, outcome_status_id, month, location_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING ";
    private static final String INSERT_STREET =
            "INSERT INTO crimes_schema.street(name) " +
                    "VALUES (?) " +
                    "ON CONFLICT DO NOTHING";
    private static final String INSERT_LOCATION =
            "INSERT INTO crimes_schema.location(latitude, longitude, id, street_name) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING";
    private static final String INSERT_OUTCOME_STATUS =
            "INSERT INTO crimes_schema.outcome_status(date, outcome_category) " +
                    "VALUES (?, ?)";

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
                            .update(INSERT_STREET)
                            .params(crime.getLocation().getStreet().getName())
                            .run();
                    query
                            .update(INSERT_LOCATION)
                            .params(crime.getLocation().getLatitude(), crime.getLocation().getLongitude(), crime.getLocation().getStreet().getId(), crime.getLocation().getStreet().getName())
                            .run();

                    Integer outcomeStatusId = null;
                    if (crime.getOutcomeStatus() != null) {
                        UpdateResultGenKeys<Integer> result = query
                                .update(INSERT_OUTCOME_STATUS)
                                .params(crime.getOutcomeStatus().getDate(), crime.getOutcomeStatus().getCategory())
                                .runFetchGenKeys(Mappers.singleInteger());
                        outcomeStatusId = result.generatedKeys().get(0);
                        crime.getOutcomeStatus().setId(outcomeStatusId);
                    }

                    query
                            .update(INSERT_CRIME)
                            .params(crime.getPersistentId(), crime.getId(), crime.getLocationType(), crime.getCategory(), crime.getContext(), crime.getLocationSubtype(), outcomeStatusId, crime.getMonth(), crime.getLocation().getStreet().getId())
                            .run();
                    return null;
                }
        );
    }
}
