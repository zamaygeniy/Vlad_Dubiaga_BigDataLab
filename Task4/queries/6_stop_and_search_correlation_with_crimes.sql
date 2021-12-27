SELECT 
	location.id AS street_id, 
	street.name AS street_name, 
	crime.month AS month,
	COUNT(CASE WHEN crime.category = 'drugs' 
			   THEN 1 
			   ELSE null 
		  END) AS drugs_crimes_count,
	COUNT(CASE WHEN stop.object_of_search = 'Controlled drugs' 
			   THEN 1
			   ELSE null 
		  END) AS drugs_stop_and_search_count,
	COUNT(CASE WHEN crime.category = 'possession-of-weapons' 
			   THEN 1 
			   ELSE null 
	      END) AS weapons_crimes_count,
	COUNT(CASE WHEN stop.object_of_search = 'Offensive weapons' 
			   OR stop.object_of_search = 'Firearms' 
			   THEN 1 
			   ELSE null 
		  END) AS weapons_stop_and_search_count,
	COUNT(CASE WHEN crime.category = 'theft-from-the-person' 
			   OR crime.category = 'shoplifting' 
			   THEN 1 
			   ELSE null 
		  END) AS theft_crimes_count,
	COUNT(CASE WHEN stop.object_of_search = 'Stolen goods' 
			   THEN 1
			   ELSE null 
		  END) AS theft_stop_and_search_count
FROM crimes_schema.crime AS crime
INNER JOIN crimes_schema.location AS location ON crime.location_id = location.id
INNER JOIN crimes_schema.street AS street ON location.street_name = street.name
INNER JOIN crimes_schema.stop AS stop ON stop.location_id = location.id
WHERE 
	stop.datetime >= :'start_date'::date 
	AND stop.datetime <= :'end_date'::date
GROUP BY location.id, street.name, crime.month
ORDER BY street.name;