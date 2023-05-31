# Getting Started

### _1. Install OpenJDK17 for your OS_

------------

#### Ubuntu

```bash
sudo apt update -y
sudo apt install openjdk-17-jdk -y
java --version

```

#### CentOS

```bash
sudo yum install java-17-openjdk -y
java --version
```

### _2. Install Gradle for your OS_

------------

```bash
wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp
sudo unzip -d /opt/gradle /tmp/gradle-7.6-bin.zip
sudo touch /etc/profile.d/gradle.sh
echo 'export PATH=/opt/gradle/gradle-7.6/bin:$PATH' | sudo tee /etc/profile.d/gradle.sh
sudo chmod +x /etc/profile.d/gradle.sh
source /etc/profile.d/gradle.sh
gradle
```

### _3. Build
```bash
# ./gradlew clean build
```
### _4. Run compiled file_

------------

```bash
# ./start.sh
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
http://serverip:8082
```
