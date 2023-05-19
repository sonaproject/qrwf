# Getting Started

### _1. Install OpenJDK17 for your OS_

------------

#### Ubuntu

```bash
sudo apt update
sudo apt install openjdk-17-jdk
java --version

```

#### CentOS

```bash
sudo yum install java-17-openjdk
java --version
```

### _2. Install Gradle for your OS_

------------
#### Ubuntu

```bash
sudo apt update
sudo apt install gradle
gradle --version

```

#### CentOS

```bash
sudo yum install epel-release
sudo yum install gradle
gradle --version
```

### _3. Compiling with Gradle_

------------
```bash
git clone https://github.com/sonaproject/qrwf.git
cd qrwf
gradle clean build
```

### _4. Run compiled file_

------------

```bash
nohup java -XX:+UseG1GC -Dlog4j2.formatMsgNoLookups=true -jar qrwf-1.0.jar > output.log 2>&1 &
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