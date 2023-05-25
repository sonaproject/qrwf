# Getting Started

### _1. Install OpenJDK17, Gradle for your OS_

------------

#### Ubuntu

```bash
sudo apt update -y
sudo apt install openjdk-17-jdk -y
java --version

sudo apt install gradle
gradle --version

```

#### CentOS

```bash
sudo yum install java-17-openjdk -y
java --version

sudo yum install gradle -y
gradle --version
```


### _2. Compiling with Gradle_

------------
```bash
git clone https://github.com/sonaproject/qrwf.git
cd qrwf
./gradlew clean build
```

### _3. Run compiled file_

------------

```bash
# -Dinflux.url --> influxDB IP:PORT you are using
# -Dinflux.username --> influxDB username you are using
# -Dinflux.password --> influxDB password you are using
# -Dinflux.token --> influxDB token you are using
# -Dinflux.org --> influxDB org you are using
nohup java -jar qrwf-1.0.jar \
      -XX:+UseG1GC \
      -Dlog4j2.formatMsgNoLookups=true \ 
      -Dinflux.url=10.10.10.10:8086 \ 
      -Dinflux.username=xxx  
      -Dinflux.password=xxx \
      -Dinflux.token=xxx \
      -Dinflux.org=xxx > output.log 2>&1 &
```

### _5. How to check logs_

------------

#### java Service Log
```bash
tail -f ~/logs/qrwf.log
```

#### Java Console Log
```bash
tail -f ./output.log
```

### _6. WEB UI connection URL_

------------
```shell
http://serverip:8081
```
