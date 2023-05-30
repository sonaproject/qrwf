#!/bin/bash

source_file="$1"
destination_dir="/home/ubuntu/qrwf/"

if [ -f "$source_file" ]; then
  cp "$source_file" "$destination_dir"
  echo "File copied successfully."
else
  echo "File does not exist. Skipping."
fi

NAME=qrwf-1.0.jar

echo ">>>>> Kill $NAME"
pkill -f $NAME

echo ">>>>> Start $NAME"

nohup java -jar /home/ubuntu/qrwf/build/libs/qrwf-1.0.jar -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true --influx.url=http://10.0.0.243:8086 --influx.username=edgecloud --influx.password=edge123! --influx.token=wsRkPDMrs0Vnu2RFU-IkFEBPxtgwqPGVD7clS3iMr9qOXrQynw0PTcMbQh7sOmLcLcJvf4-wcl1PtvZrYDSGiw== --influx.org=edgecloud > output.log 2>&1
