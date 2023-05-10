#!/bin/bash

 NAME=qrwf-1.0.jar

echo ">>>>> Kill $NAME"
pkill -f $NAME

echo ">>>>> Start $NAME"

nohup java -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true /home/ec2-user/qrwf/$NAME > output.log 2>&1 &
