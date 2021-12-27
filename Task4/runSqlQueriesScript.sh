#!/bin/bash

QUERY_INDEX_REGEX="^1|2|3|4|5|6$"
ROWS_NUM_REGEX="^[0-9]+$"

START_DATE="2021-01-01"
END_DATE="2021-05-01"
OUTCOME_CATEGORY="Investigation complete; no suspect identified"

output_file="result"
query_index=0
rows_num=10

print_usage () {
	echo "$0: usage: [-h] [-f file] [-i index] [-r rows]"
}

print_help () {
	print_usage
	echo "Options:"
	echo "	-h			show help message"
	echo "	-f [file]		name of the output file"
	echo "				default output file is 'result'"
	echo "	-i [index]		index of sql script"
	echo "	-r [rows]		printed rows number, default 10"
}


while getopts "hf:i:r:" opt; do
        case $opt in
                h)
                        print_help
                        exit 0;;
                \?)
                        print_usage
			exit 1;;
                f)
                        output_file=${OPTARG};;
		i)
			if ! [[ ${OPTARG} =~ $QUERY_INDEX_REGEX ]]; then
                		echo "$0: invalid script index: ${OPTARG}" >&2
				print_usage
				exit 1 
        		fi
			query_index=${OPTARG};;
		r)
			if ! [[ ${OPTARG} =~ $ROWS_NUM_REGEX ]]; then
				echo "$0: invalid rows number: ${OPTARG}" >&2
				print_usage
				exit 1
			fi
			rows_num=${OPTARG};;
	esac
done

if [ "$query_index" -eq 0 ]; then
	print_usage
	echo "$query_index"
	exit 1;
fi

case "$query_index" in
	1)
		psql -d crimes -f "queries/1_most_dangerous_streets.sql" -v start_date=$START_DATE -v end_date=$END_DATE > "$output_file";;
	2)
		psql -d crimes -f "queries/2_month_to_month_crime_volume_comparison.sql" -v start_date=$START_DATE -v end_date=$END_DATE > "$output_file";;
	3)
		psql -d crimes -f "queries/3_crimes_with_specified_outcome_status.sql" -v start_date=$START_DATE -v end_date=$END_DATE -v outcome_category="$OUTCOME_CATEGORY" > "$output_file";;
	4)
		psql -d crimes -f "queries/4_stop_and_search_statistics_by_ethnicity.sql" -v start_date=$START_DATE -v end_date=$END_DATE > "$output_file";;
	5)
		psql -d crimes -f "queries/5_most_probable_stop_and_search_snapshot_on_street_level.sql" -v start_date=$START_DATE -v end_date=$END_DATE > "$output_file";;
	6)
		psql -d crimes -f "queries/6_stop_and_search_correlation_with_crimes.sql" -v start_date=$START_DATE -v end_date=$END_DATE > "$output_file";;
esac

(( rows_num += 2 ))
head -n $rows_num "$output_file"
