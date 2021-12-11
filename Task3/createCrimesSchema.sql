
CREATE SCHEMA crimes_schema

CREATE TABLE crimes_schema.street
(
	id integer NOT NULL PRIMARY KEY,
	name character varying(100) NOT NULL
);

CREATE TABLE crimes_schema.location
(
	latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    street_id integer NOT NULL REFERENCES crimes_schema.street(id),
    PRIMARY KEY(latitude, longitude)
);

CREATE TABLE crimes_schema.category
(
	url character varying(60) NOT NULL PRIMARY KEY,
    name character varying(60) NOT NULL
);

CREATE TABLE crimes_schema.outcome_category
(
    code character varying(45) NOT NULL PRIMARY KEY,
    name character varying(51) NOT NULL
);

CREATE TABLE crimes_schema.outcome_status
(
    id serial NOT NULL PRIMARY KEY,
    date date NOT NULL,
    category character varying(45) NOT NULL REFERENCES crimes_schema.outcome_category(code)
);

CREATE TABLE crimes_schema.crime
(
    persistent_id character varying(64),
    id integer NOT NULL,
    location_type character varying(5) NOT NULL,
    category character varying(45) NOT NULL REFERENCES crimes_schema.category(url),
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    context text,
    location_subtype character varying(45),
    outcome_status integer REFERENCES crimes_schema.outcome_status(id),
    month date NOT NULL,
    FOREIGN KEY (latitude, longitude) REFERENCES crimes_schema.location(latitude, longitude),
    PRIMARY KEY (persistent_id, id)
);
