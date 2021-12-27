#!/bin/bash

DATE_REGEX="^((19|20)[0-9]{2})-(0[1-9]|1[012])$"
API_REGEX="^crimes-street|stops-force$"

output="/dev/null"
clear_flag=false
package_flag=false
path=""
from=""
to=""
api=""
force=""

print_usage () {
	 echo "$0: usage: [-h] [-v] [-c] [-p] [-t [path]] [-r [from] [-o [to]] [-a [api method]] [-e [force]]"
}

while getopts "hvcpt:r:o:a:e:" opt; 
do	
	echo "$opt"
        case $opt in
		h)
                        echo "$0 - script for writing crimes data into db"
			print_usage
			echo "Options:"
			echo "	-h		print help message"
			echo "	-v		enable non-script output"
			echo "	-c		clear database"
			echo "	-p		package maven project"
			echo "	-t [path]	coordinates file for 'crimes-street' method"
			echo "	-r [from]	select start date in format yyyy-MM"
			echo "	-o [to]		select end date in format yyyy-MM"
			echo "	-a [api method]	select api method. 'crimes-street' or 'stop-forces'"
			echo "	-e [force] 	select force for 'stop-forces' method"
                        exit 0;;
                v)
                        output="/dev/stdout";;
                c)
                        clear_flag=true;;
                p)
                        package_flag=true;;
                t)
                        path=${OPTARG}
                        if [ ! -f "$path" ]; then
                        	echo "$0: file not found: $path"
                        	exit 1
                        fi;;
                r)
                        from=${OPTARG}
                        if ! [[ $from =~ $DATE_REGEX ]]; then
                        	echo "$0: invalid date: $from"
                        	exit 1
                        fi;;
               	o)
			to=${OPTARG}
                        if ! [[ $to =~ $DATE_REGEX ]]; then
                        	echo "$0: invalid date: $to"
                        	exit 1
                        fi;;
		a)
			api=${OPTARG}
			if ! [[ $api =~ $API_REGEX ]]; then
				echo "$0: invalid api method: $api"
				exit 1
			fi;;
		e)
			force=${OPTARG};;
		\?)
			print_usage
			exit 1;;
        esac
done

if [ "$(which psql 2>&1 | wc -w)" -ne 1 ]
then 
	echo "Installing PostgreSQL..."
	sudo dnf install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-8-x86_64/pgdg-redhat-repo-latest.noarch.rpm -y
	sudo dnf -qy module disable postgresql
	sudo dnf install -y postgresql14-server
	sudo /usr/pgsql-14/bin/postgresql-14-setup initdb
	sudo systemctl enable postgresql-14
	sudo systemctl start postgresql-14
	echo "PostgreSQL installation is complete."
else
	echo "PostgreSQL is already installed"
fi

psql crimes -f "queries/createCrimesSchema.sql" >$output 2>&1 
psql crimes -f "queries/createCrimesTables.sql" >$output 2>&1


if $clear_flag ;then
	psql -d crimes -f "queries/deleteDataFromCrimesSchema.sql" > $output 2>&1
	echo "crimes_schema tables was cleared"
fi

if $package_flag ;then
	mvn package > $output 2>&1
	echo "Project was packaged"
fi

case $api in
	"crimes-street")
		if [[ -z "$from" || -z "$to" || -z "$path" ]]; then
			print_usage
			exit 1
		else	
			java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dapi="$api" -Dpath="$path" -Dfrom="$from" -Dto="$to" -Dwrite=db -Dcategory=all-crimes > $output 2>&1
		fi;;
	"stops-force")
		if [[ -z "$from" || -z "$to" ]]; then
                        print_usage
                        exit 1
                fi
		echo "Loading stops data..."
		if [ -z "$force" ]; then
			java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dapi="$api" -Dfrom="$from" -Dto="$to" -Dforce=all -Dwrite=db > $output 2>&1
		else
			java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dapi="$api" -Dfrom="$from" -Dto="$to" -Dforce="$force" -Dwrite=db > $output 2>&1
		fi;;
	*)
		echo "$0: invalid api option";;
esac
exit 0

