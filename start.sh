#!/bin/bash

source_file="$1"
destination_dir="/home/ec2-user/qrwf/"

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

nohup java -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true -jar /home/ec2-user/qrwf/$NAME > output.log 2>&1 &
