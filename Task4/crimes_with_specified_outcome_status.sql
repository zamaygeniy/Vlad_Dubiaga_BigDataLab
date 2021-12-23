SELECT location.id AS street_id, street.name AS street_name,
'Unable to prosecute suspect' AS outcome_category,
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
WHERE crime.month > '2021-01-01'::date AND crime.month < '2021-05-01'::date
AND outcome.outcome_category = 'Unable to prosecute suspect'
GROUP BY location.id, street.name;