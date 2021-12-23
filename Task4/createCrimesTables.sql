CREATE SCHEMA IF NOT EXISTS crimes_schema;
CREATE TABLE IF NOT EXISTS crimes_schema.street
(
	    name character varying(100) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS crimes_schema.location
(
	    latitude double precision NOT NULL,
	    longitude double precision NOT NULL,
	    id integer PRIMARY KEY,
	    street_name  character varying(100) NOT NULL REFERENCES crimes_schema.street(name)
);

CREATE TABLE IF NOT EXISTS crimes_schema.outcome_status
(
	    id serial PRIMARY KEY,
	    date date NOT NULL,
	    outcome_category character varying(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS crimes_schema.crime
(
	    persistent_id character varying(64),
	    id integer PRIMARY KEY,
	    location_type character varying(5) NOT NULL,
	    category character varying(60) NOT NULL,
	    context text,
	    location_subtype character varying(60),
	    outcome_status_id integer REFERENCES crimes_schema.outcome_status(id),
	    month date NOT NULL,
	    location_id integer REFERENCES crimes_schema.location(id)
);

CREATE TABLE IF NOT EXISTS crimes_schema.outcome_object
(
	id character varying(50) PRIMARY KEY,
	name character varying(50)
);

CREATE TABLE IF NOT EXISTS crimes_schema.stop
(
	type character varying(100),
	involved_person boolean,
	datetime timestamp PRIMARY KEY, 
	operation boolean,
	operation_name character varying(100),
	location_id integer REFERENCES crimes_schema.location(id),
	gender character varying(10),
	age_range character varying(15),
	self_defined_ethnicity character varying(100),
	officer_defined_ethnicity character varying(100),
	legislation character varying(100),
	object_of_search character varying(100), 
	outcome_object_id character varying(100) REFERENCES crimes_schema.outcome_object(id),
	outcome_linked_to_object_of_search boolean,
	removal_of_more_than_outer_clothing boolean
);
