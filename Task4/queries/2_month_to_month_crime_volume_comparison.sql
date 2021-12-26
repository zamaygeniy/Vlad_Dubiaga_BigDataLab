WITH cte AS (
	SELECT crime.category AS category, crime.month AS month, COUNT(CASE 
											  WHEN crime.month >= :'start_date'::date AND crime.month <= :'end_date'::date THEN 1
											  ELSE NULL
											  END) AS current_month_count
	FROM crimes_schema.crime
	GROUP BY crime.category, crime.month
	ORDER BY crime.category DESC
), cte2 AS (
	SELECT category, month,
	(LAG(current_month_count, 1) OVER (PARTITION BY category ORDER BY category, month)) AS previous_month_count,
	current_month_count 
	FROM cte
)
SELECT category, month, previous_month_count, current_month_count, 
(current_month_count - previous_month_count) delta_count, 
ROUND((current_month_count - previous_month_count) * 100.0 / previous_month_count, 4) basic_growth_rate
FROM cte2;

