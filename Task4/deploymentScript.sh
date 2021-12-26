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
	 echo "$0: usage: [-h] [-v] [-c] [-pg] [-p [path]] [-from] [-to] [-a [api method]] [-fc [force]]"
}

while getopts "hvcpgp:from:to:a:fc:" opt; 
do
        case $opt in
				h)
                        echo "$0 - help message"
                        exit 0;;
                v)
                        output="/dev/stdout";;
                c)
                        clear_flag=true;;
                pg)
                        package_flag=true;;
                p)
                        path=${OPTARG}
                        if [ ! -f "$path" ]; then
                        	echo "$0: file not found: $file"
                        	exit 1
                        fi;;
                from)
                        from=${OPTARG}
                        if ! [[ $from =~ $DATE_REGEX ]]; then
                            echo "$0: invalid date: $from"
                            exit 1
                        fi;;
               	to)
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
				fc)
						force=${OPTARG};;
				\?)
						print_usage
						exit 1;;
        esac
done

if [ "$(which psql 2>&1 | wc -w)" -ne 1 ]
then 
	echo "Installing PostgreSQL..."
	sudo dnf install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-8-x86_64/pgdg-redhat-repo-latest.noarch.rpm -y > $output 2>&1
	sudo dnf -qy module disable postgresql > $output 2>&1
	sudo dnf install -y postgresql14-server
	sudo /usr/pgsql-14/bin/postgresql-14-setup initdb
	sudo systemctl enable postgresql-14
	sudo systemctl start postgresql-14
	echo "PostgreSQL installation is complete."
else
	echo "PostgreSQL is already installed"
fi

psql crimes -f createCrimesSchema.sql >$output 2>&1 
psql crimes -f createCrimesTables.sql >$output 2>&1


if $clear_flag ;then
	psql -d crimes -f deleteDataFromCrimesSchema.sql > $output 2>&1
	echo "crimes_schema tables was cleared"
fi

if $package_flag ;then
	mvn package > $output 2>&1
	echo "Project was packaged"
fi

case $api in
	"crimes-street")
		if [[ -z "$date" ||  -z "$path" ]]; then
			print_usage
			exit 1
		else
			echo "Loading crimes data..."
			java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dapi=$api -Dpath=$path -Dfrom=$from -Dto=$to > $output 2>&1
		fi;;
	"stops-force")
		if [ -z "$date" ]; then
                        print_usage
                        exit 1
                fi
		echo "Loading stops data..."
		if [ -z "$force" ]; then
			java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dapi=$api -Dfrom=$from -Dto=$to -Dforce="all" > $output 2>&1
		else
			java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dapi=$api -Dfrom=$from -Dto=$to -Dforce=$force > $output 2>&1
		fi;;
	*)
		echo "$0: invalid api option";;
esac
exit 0

