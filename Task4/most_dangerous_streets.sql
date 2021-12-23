SELECT location.id AS street_id, street.name AS street_name, 'from 2021-01 till 2021-05' AS period, COUNT(*) AS crime_count
FROM crimes_schema.street AS street
JOIN crimes_schema.location AS location
ON street.name = location.street_name
JOIN crimes_schema.crime AS crime
ON location.id = crime.location_id
WHERE crime.month > '2021-01-01'::date AND crime.month < '2021-03-01'::date
GROUP BY street.name, location.id
ORDER BY crime_count DESC;

