package com.epam.crimes.dao.impl;

import com.epam.crimes.connection.DatabaseConnection;
import com.epam.crimes.dao.Dao;
import com.epam.crimes.entity.Stop;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StopDao implements Dao<Stop> {

    private FluentJdbc fluentJdbc = DatabaseConnection.getInstance().getFluentJdbc();


    private static final String INSERT_STOP =
            "INSERT INTO crimes_schema.stop(type, involved_person, datetime, operation, operation_name, location_id, gender, age_range, self_defined_ethnicity, officer_defined_ethnicity, legislation, object_of_search, outcome_object_id, outcome_linked_to_object_of_search, removal_of_more_than_outer_clothing) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING ";
    private static final String INSERT_STREET =
            "INSERT INTO crimes_schema.street(name) " +
                    "VALUES (?) " +
                    "ON CONFLICT DO NOTHING";
    private static final String INSERT_LOCATION =
            "INSERT INTO crimes_schema.location(latitude, longitude, id, street_name) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING";
    private static final String INSERT_OUTCOME_OBJECT =
            "INSERT INTO crimes_schema.outcome_object(id, name ) " +
                    "VALUES (?, ?) " +
                    "ON CONFLICT DO NOTHING";


    @Override
    public List<Stop> findAll() {
       /* Query query = fluentJdbc.query();
        return query
                .select(FIND_ALL)
                .listResult(manualStopMappper);*/
        return null;
    }

    @Override
    public void create(List<Stop> stops) {
        for (Stop stop : stops) {
            this.create(stop);
        }
    }

    @Override
    public void create(Stop stop) {
        Query query = fluentJdbc.query();

        query.transaction().in(
                () -> {
                    Integer streetId = null;
                    if (stop.getLocation() != null) {
                        query
                                .update(INSERT_STREET)
                                .params(stop.getLocation().getStreet().getName())
                                .run();
                        query
                                .update(INSERT_LOCATION)
                                .params(stop.getLocation().getLatitude(), stop.getLocation().getLongitude(), stop.getLocation().getStreet().getId(), stop.getLocation().getStreet().getName())
                                .run();
                        streetId = stop.getLocation().getStreet().getId();
                    }

                    query
                            .update(INSERT_OUTCOME_OBJECT)
                            .params(stop.getOutcomeObject().getId(), stop.getOutcomeObject().getName())
                            .run();



                    query
                            .update(INSERT_STOP)
                            .params(stop.getType(), stop.getInvolvedPerson(),
                                    formatDate(stop.getDatetime()), stop.getOperation(),
                                    stop.getOperationName(), streetId,
                                    stop.getGender(), stop.getAgeRange(), stop.getSelfDefinedEthnicity(),
                                    stop.getOfficerDefinedEthnicity(), stop.getLegislation(), stop.getObjectOfSearch(),
                                    stop.getOutcomeObject().getId(), stop.getOutcomeLinkedToObjectOfSearch(), stop.getRemovalOfMoreThanOuterClothing())
                            .run();
                    return null;
                }
        );
    }

    private LocalDateTime formatDate(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.substring(0, 19), dtf);
    }


}
