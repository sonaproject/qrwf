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

#nohup java -jar /home/ubuntu/qrwf/build/libs/qrwf-1.0.jar -XX:+PrintCompilation -XX:CompileCommand=exclude,*.CPULoadGenerator -Xint -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true -Dinflux.url=10.0.0.243:8086 -Dinflux.username=edgecloud -Dinflux.password=edge123! -Dinflux.token=wsRkPDMrs0Vnu2RFU-IkFEBPxtgwqPGVD7clS3iMr9qOXrQynw0PTcMbQh7sOmLcLcJvf4-wcl1PtvZrYDSGiw== -Dinflux.org=edgecloud > output.log 2>&1

nohup java -XX:+PrintCompilation -XX:CompileCommand=compileonly,*.indexOf  -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true -Dinflux.url=10.0.0.243:8086 -Dinflux.username=edgecloud -Dinflux.password=edge123! -Dinflux.token=wsRkPDMrs0Vnu2RFU-IkFEBPxtgwqPGVD7clS3iMr9qOXrQynw0PTcMbQh7sOmLcLcJvf4-wcl1PtvZrYDSGiw== -Dinflux.org=edgecloud -jar /home/ubuntu/qrwf/build/libs/qrwf-1.0.jar > output.log 

#nohup java -Xint -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true -Dinflux.url=10.0.0.243:8086 -Dinflux.username=edgecloud -Dinflux.password=edge123! -Dinflux.token=wsRkPDMrs0Vnu2RFU-IkFEBPxtgwqPGVD7clS3iMr9qOXrQynw0PTcMbQh7sOmLcLcJvf4-wcl1PtvZrYDSGiw== -Dinflux.org=edgecloud -jar /home/ubuntu/qrwf/build/libs/qrwf-1.0.jar > output.log 2>&1

