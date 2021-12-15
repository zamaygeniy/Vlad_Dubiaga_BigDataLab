#!/bin/bash
output="/dev/null"
while getopts "hv" opt
do
	case $opt in
		h)		
			echo "$0 - this is an enviroment setup script"
			echo "usage: $0         install all programms"
			echo "   or: $0 [param] install only specified programms"
			echo -n
			echo "Options:"
			echo "-v		       	   show installation output"
			echo "-h		       	   show help"
			echo -n
			echo "Parameters:"
			echo "java         	       	   install Java"
			echo "git	 	       	   install Git"
			echo "maven 		       	   install Maven"
			echo "postgresql	       	   install PostgreSQL"
			exit 0
			;;
		v)
			output="/dev/stdout"
			;;
	esac
done	
function installJava {
	if [ "$(find /usr/lib/jvm -maxdepth 1 -type d -name "java-11*" | wc -l)" -eq 0 ]
	then
		echo "Installing Java..."
		dnf install java-11-openjdk-devel -y > $output
		java_home=$(find /usr/lib/jvm -maxdepth 1 -type d -name "java-11*")
		ln -sfn "$java_home/bin/java" /etc/alternatives/java
		echo "Java installation is complete."
	else
		echo "Java is already installed."
	fi
}
function installGit {
  if [ "$(which git 2>&1 | wc -w)" -ne 1 ]
	then
		echo "Installing Git..."
		dnf install git -y > $output
		echo "Git installation is complete."
	else
		echo "Git is already installed"
	fi
}
function installMaven {
	if [ "$(which mvn 2>&1 | wc -w)" -ne 1 ]
	then
		echo "Installing Maven..."
		dnf install maven -y > $output
		if [ "$(find /usr/lib/jvm -maxdepth 1 -type d -name "java-11*" | wc -l)" -ne 0 ]
		then
			ln -sfm "$(find /usr/lib/jvm -maxdepth 1 -type d -name "java-11*")/bin/java" /etc/alternatives/java
		fi
		echo "Maven installation is complete."
	else
		echo "Maven is already installed"
	fi
}
function installPostgreSQL {
	if [ "$(which psql 2>&1 | wc -w)" -ne 1 ]
	then
		echo "Installing PostgreSQL..."
		dnf install https://download.postgresql.org/pub/repos/yum/reporpms/EL-8-x86_64/pgdg-redhat-repo-latest.noarch.rpm -y > $output
		dnf install postgresql12 postgresql12-server -y > $output
		echo "PostgreSQL installation is complete."
	else
		echo "PostgreSQL is already installed"
	fi
}


arg_regex="^git|java|maven|postgresql|-v$"

if [ $# -eq 0 ] || [ $# -eq 1 ] && [ "$1" = "-v" ]
then
	installJava
	installGit
	installMaven
	installPostgreSQL
	exit 0
fi

for arg in "$@"
do
	if ! [[ $arg =~ $arg_regex ]]
	then
		>&2 echo "$0: $arg: invalid argument"
		exit 1
	fi
done

for arg in "$@"
do
	case $arg in
		java)
			installJava
			;;
		git)
			installGit
			;;
		maven)
			installMaven
			;;
		postgresql)
			installPostgreSQL
			;;
	esac
done
exit 0
