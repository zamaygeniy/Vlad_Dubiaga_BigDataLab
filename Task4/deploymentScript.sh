#!/bin/bash
output="/dev/null"
clear_flag=false
while getopts "hvc" opt
do
	case $opt in
		h)
			echo "$0 - this script writes data to database crimes"
			echo "usage:"
			;;
		v)
			output="/dev/stdout"
			;;
		c)

	esac
done

if [ "$(which psql 2>&1 | wc -w)" -ne 1 ]
then 
	echo "Installing PostgreSQL..."
	sudo dnf install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-8-x86_64/pgdg-redhat-repo-latest.noarch.rpm -y > $output
	sudo dnf -qy module disable postgresql > $output
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

java -cp target/lib/crimes-1.0-SNAPSHOT-jar-with-dependencies.jar com.epam.crimes.control.Main -Dpath=LondonStations.csv
