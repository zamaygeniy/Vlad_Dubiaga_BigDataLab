CREATE SCHEMA crimes_schema

CREATE TABLE crimes_schema.street
(
    id integer NOT NULL PRIMARY KEY ,
    name character varying(100) NOT NULL
);

CREATE TABLE crimes_schema.location
(
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    street_id integer NOT NULL REFERENCES crimes_schema.street(id),
    PRIMARY KEY(latitude, longitude)
);

CREATE TABLE crimes_schema.outcome_status
(
    id serial PRIMARY KEY,
    date date NOT NULL,
    outcome_category character varying(60) NOT NULL
);

CREATE TABLE crimes_schema.crime
(
    persistent_id character varying(64),
    id integer PRIMARY KEY ,
    location_type character varying(5) NOT NULL,
    category character varying(60) NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    context text,
    location_subtype character varying(60),
    outcome_status_id integer REFERENCES crimes_schema.outcome_status(id),
    month date NOT NULL,
    FOREIGN KEY (latitude, longitude) REFERENCES crimes_schema.location(latitude, longitude)
);
