package com.epam.crimes.dao.impl;

import com.epam.crimes.dao.CrimeDao;
import com.epam.crimes.entity.Crime;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import static com.epam.crimes.dao.ColumnName.*;

import java.util.List;

public class CrimeDaoImpl extends CrimeDao {

    private static final Mapper<Crime> manualCrimeMappper = resultSet -> {
        Crime crime = new Crime();
        crime.setPersistentId(resultSet.getString(PERSISTENT_ID));
        crime.setId(resultSet.getInt(ID));
        crime.setLocationType(resultSet.getString(LOCATION_TYPE));
        crime.setCategory(resultSet.getString(CATEGORY));
        crime.getLocation().setLatitude(resultSet.getDouble(LATITUDE));
        crime.getLocation().setLongitude(resultSet.getDouble(LONGITUDE));
        crime.setContext(resultSet.getString(CONTEXT));
        crime.setLocationSubtype(resultSet.getString(LOCATION_SUBTYPE));
        crime.setMonth(resultSet.getDate(MONTH));
        if (resultSet.getDate(DATE) != null) {
            crime.getOutcomeStatus().setId(resultSet.getInt(OUTCOME_STATUS_ID));
            crime.getOutcomeStatus().setDate(resultSet.getDate(DATE));
            crime.getOutcomeStatus().setCategory(resultSet.getString(OUTCOME_CATEGORY));
        } else {
            crime.setOutcomeStatus(null);
        }
        crime.getLocation().getStreet().setId(resultSet.getInt(STREET_ID));
        crime.getLocation().getStreet().setName(resultSet.getString(NAME));
        return crime;
    };
    private static final String FIND_ALL =
            "SELECT * FROM crimes_schema.crime " +
            "LEFT JOIN crimes_schema.outcome_status " +
            "ON crimes_schema.crime.outcome_status_id = crimes_schema.outcome_status.id " +
            "JOIN crimes_schema.location " +
            "ON crimes_schema.crime.latitude = crimes_schema.location.latitude " +
            "AND crimes_schema.crime.longitude = crimes_schema.location.longitude " +
            "JOIN crimes_schema.street " +
            "ON crimes_schema.location.street_id = crimes_schema.street.id";
    private static final String INSERT_CRIME =
            "INSERT INTO crimes_schema.crime(persistent_id, id, location_type, category, latitude, longitude, context, location_subtype, outcome_status_id, month) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT DO NOTHING ";
    private static final String INSERT_STREET =
            "INSERT INTO crimes_schema.street(name, id) " +
            "VALUES (?, ?) " +
            "ON CONFLICT DO NOTHING";
    private static final String INSERT_LOCATION =
            "INSERT INTO crimes_schema.location(latitude, longitude, street_id) " +
            "VALUES (?, ?, ?) " +
            "ON CONFLICT DO NOTHING";

    private static final String INSERT_OUTCOME_STATUS =
            "INSERT INTO crimes_schema.outcome_status(date, outcome_category) " +
            "VALUES (?, ?)";

    public CrimeDaoImpl(FluentJdbc fluentJdbc) {
        super(fluentJdbc);
    }

    @Override
    public List<Crime> findAll() {
        Query query = fluentJdbc.query();
        return query
                .select(FIND_ALL)
                .listResult(manualCrimeMappper);
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
                            .update(INSERT_STREET)
                            .params(crime.getLocation().getStreet().getName(), crime.getLocation().getStreet().getId())
                            .run();
                    query
                            .update(INSERT_LOCATION)
                            .params(crime.getLocation().getLatitude(), crime.getLocation().getLongitude(), crime.getLocation().getStreet().getId())
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
                            .params(crime.getPersistentId(), crime.getId(), crime.getLocationType(), crime.getCategory(), crime.getLocation().getLatitude(), crime.getLocation().getLongitude(), crime.getContext(), crime.getLocationSubtype(), outcomeStatusId, crime.getMonth())
                            .run();
                    return null;
                }
        );
    }
}
