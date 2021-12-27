SELECT 
	street.name,
	MODE() WITHIN GROUP (ORDER BY stop.age_range) AS most_popular_age_range,
	MODE() WITHIN GROUP (ORDER BY stop.gender) AS most_popular_gender,
	MODE() WITHIN GROUP (ORDER BY stop.officer_defined_ethnicity) AS most_popular_ethnicity,
	MODE() WITHIN GROUP (ORDER BY stop.object_of_search) AS most_popular_object_of_search,
	MODE() WITHIN GROUP (ORDER BY stop.outcome_object_id) AS most_popular_outcome
FROM crimes_schema.stop AS stop
JOIN crimes_schema.location AS location ON stop.location_id = location.id
JOIN crimes_schema.street AS street ON location.street_name = street.name
WHERE 
	stop.datetime >= :'start_date'::date 
	AND stop.datetime <= :'end_date'::date
GROUP BY street.name
ORDER BY street.name