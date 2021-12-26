SELECT location.id AS street_id, street.name AS street_name,
:'outcome_category' AS outcome_category,
COUNT(*) AS crime_count,
COUNT(*) * 100.0 / SUM(COUNT(*)) OVER() AS percentage
FROM crimes_schema.crime 
JOIN
crimes_schema.outcome_status AS outcome
ON crime.outcome_status_id = outcome.id
JOIN
crimes_schema.location AS location
ON crime.location_id = location.id
JOIN
crimes_schema.street AS street
ON location.street_name = street.name
WHERE crime.month > :'start_date'::date AND crime.month < :'end_date'::date
AND outcome.outcome_category = :'outcome_category'
GROUP BY location.id, street.name;