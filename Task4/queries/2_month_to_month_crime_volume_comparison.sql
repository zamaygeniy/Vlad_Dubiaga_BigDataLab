WITH crimes_for_month AS (
	SELECT 
		crime.category AS category, 
		crime.month AS month, 
		COUNT(CASE WHEN crime.month >= :'start_date'::date 
				   AND crime.month <= :'end_date'::date 
				   THEN 1
				   ELSE NULL
			  END) AS current_month_count
	FROM crimes_schema.crime
	GROUP BY crime.category, crime.month
	ORDER BY crime.category DESC
), crimes_for_two_months AS (
	SELECT 
		category, 
		month,
		(LAG(current_month_count, 1) OVER (PARTITION BY category ORDER BY category, month)) AS previous_month_count,
		current_month_count 
	FROM crimes_for_month
)
SELECT 
	category, 
	month, 
	previous_month_count, 
	current_month_count, 
	(current_month_count - previous_month_count) AS delta_count, 
	round((current_month_count - previous_month_count) * 100.0 / previous_month_count, 4) AS basic_growth_rate
FROM crimes_for_two_months;

