SELECT location.id AS street_id, street.name AS street_name, 'from ':'start_date'' to ':'end_date' AS period, COUNT(*) AS crime_count
FROM crimes_schema.street AS street
JOIN crimes_schema.location AS location
ON street.name = location.street_name
JOIN crimes_schema.crime AS crime
ON location.id = crime.location_id
WHERE crime.month >= :'start_date'::date AND crime.month <= :'end_date'::date
GROUP BY street.name, location.id
ORDER BY crime_count DESC;

