SELECT stop.officer_defined_ethnicity,
COUNT(*) AS total_number,
round(
	COUNT(CASE WHEN outcome_object.name = 'Arrest' THEN 1 ELSE null END) * 100.0 / COUNT(*), 2
) AS arrest_percentage,
round(
	COUNT(CASE WHEN outcome_object.name = 'A no further action disposal' THEN 1 ELSE null END) * 100.0 / COUNT(*), 2
) AS no_action_percentage,
round(
	COUNT(CASE WHEN outcome_object.name != 'A no further action disposal' AND outcome_object.name != 'Arrest'  THEN 1 ELSE null END) * 100.0 / COUNT(*), 2
) AS other_percentage,
most_popular.object_of_search AS most_popular_object_of_search

FROM crimes_schema.stop AS stop
JOIN crimes_schema.outcome_object
ON stop.outcome_object_id = outcome_object.id

LEFT JOIN 
(SELECT stop.officer_defined_ethnicity, stop.object_of_search,
COUNT(*) AS number_of_stops, RANK() OVER(PARTITION BY stop.officer_defined_ethnicity ORDER BY COUNT(*) DESC)
FROM crimes_schema.stop AS stop
WHERE stop.datetime >= '2021-01-01'::date AND stop.datetime <= '2021-05-01'::date
GROUP BY stop.officer_defined_ethnicity, stop.object_of_search) AS most_popular
ON stop.officer_defined_ethnicity = most_popular.officer_defined_ethnicity AND most_popular.rank = 1

WHERE stop.datetime >= '2021-01-01'::date AND stop.datetime <= '2021-05-01'::date
GROUP BY stop.officer_defined_ethnicity, most_popular.object_of_search;