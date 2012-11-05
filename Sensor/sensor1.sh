#!/bin/bash

##########################################################
# Config
 
SERVER="localhost"
PORT=8001
RESOURCEID="myComputer"
METRIC="temperature"
 
##########################################################
# Main
 
exec 3<>/dev/tcp/${SERVER}/${PORT}

#nieskonczona petla
while [ 1 -le 10 ] ; do
	#wylosuj wartosc
	let VALUE=$RANDOM%100

	#wyslij dane na port
	echo -e "${RESOURCEID}:${METRIC}:${VALUE}" >&3
	#pokaz dane w konsoli
	echo -e "${RESOURCEID}:${METRIC}:${VALUE}"

	#poczekaj losowy czas (w sekundach)
	let TIME=$RANDOM%8
	sleep $TIME
done


