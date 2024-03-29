package com.goblin.qrwf.service;

import com.goblin.qrwf.dto.ActuatorDto;
import com.goblin.qrwf.dto.JvmMetricDto;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;


import java.io.*;
import java.lang.management.MemoryMXBean;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Random;

/**
 * packageName : com.goblin.qrwf
 * fileName : service
 * author : goodhyoju
 * date : 2023/05/10 3:16 PM
 * description :
 */
@Service
public class MonitoringService {
    private static final Logger log = LoggerFactory.getLogger(MonitoringService.class);

    @Value("${influx.token}")
    private String token;

    @Value("${influx.bucket}")
    private String bucket;

    @Value("${influx.org}")
    private String org;

    @Value("${influx.url}")
    private String url;

    @Value("${server.port}")
    private String server_port;

    private InfluxDBClient client;

    @Autowired
    private Gson gson;

    private List<String> logLine;


    @PostConstruct
    public void init(){
        this.client = InfluxDBClientFactory.create(this.url, this.token.toCharArray());
        log.debug("InfluxDB Server : {}",this.url);
       // this.logLine = this.getRandomLineFromClasspath();
    }
    @PreDestroy
    public void cleanup() {
        this.client.close();
    }

    /**
     *
     * @return
     */
    public List<String> getRandomLineFromClasspath(){
        List<String> lines = new ArrayList<>();

        try {
            /*File file    =  new File("./data/test.log");
            BufferedReader reader  =  new BufferedReader(new InputStreamReader(new FileInputStream(file),"euc-kr"));*/
            // ClassPathResource를 사용하여 classpath에 있는 파일 리소스를 가져옵니다.
            Resource resource = new ClassPathResource("data/test.log");

            // 파일 리소스의 InputStream을 가져와서 문자열로 변환합니다.
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), "euc-kr");
            BufferedReader bufferedReader = new BufferedReader(reader);


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        log.debug("Get Line Count: {}",lines.size());
        return lines;
    }

    public String notDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        return  formattedDateTime;
    }

    /**
     *
     */
    public String writerMetric(){
        log.debug("Monitoring start.. {}",notDate());
        ArrayList<ActuatorDto> list = this.getMetrics();
        List<Point> points = new ArrayList<>();

        for(ActuatorDto data: list){
            points.add(Point.measurement("monitoring")
                    .addTag("service", getLocalHost())
                    .addField(data.getName(), data.getValue())
                    .time(Instant.now(), WritePrecision.NS));
        }
        try (WriteApi writeApi = client.getWriteApi()) {
            for (Point point : points) {
                writeApi.writePoint(bucket, org, point);
            }
            writeApi.flush();
            writeApi.close();
      //      log.debug("end save data: {}",points.toString());
        } catch (Exception e) {
           log.error(e.getMessage());
        }
        log.debug("Monitoring end.. {}",notDate());
        return String.valueOf(points.size());
    }

    /**
     *
     * @return
     */
    public ArrayList<ActuatorDto> getMetrics(){
        ArrayList<ActuatorDto> result = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        // JVM
        String[] metricName = {"jvm.memory.used","jvm.memory.committed","jvm.gc.memory.allocated","jvm.gc.memory.promoted"};
        try {
            for(String metric: metricName){
                try {
                    ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"+server_port+"/management/metrics/"+metric, String.class);
                    String healthData = response.getBody();
                    JvmMetricDto data = gson.fromJson(healthData,JvmMetricDto.class);
                    result.add(ActuatorDto.builder().name(metric).value(data.getMeasurements().get(0).getValue()).build());
                }catch (Exception e){
                    log.error("jvm monitoring error name:{}",metric);
                    continue;
                }

            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        //CPU, MEM
        try{
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            double systemLoadAverage = osBean.getSystemLoadAverage();

            int processors = osBean.getAvailableProcessors();
            double cpuUsage = systemLoadAverage / processors;
            long cpuUsagePercent = (long)(cpuUsage * 100);

            result.add(ActuatorDto.builder().name("cpu.usage.loadAvg").value((long)systemLoadAverage).build());
            result.add(ActuatorDto.builder().name("cpu.usage.percent").value(cpuUsagePercent).build());


            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            double memoryUsage = (double) usedMemory / maxMemory * 100;


            result.add(ActuatorDto.builder().name("memory.usage").value(usedMemory).build());
            result.add(ActuatorDto.builder().name("memory.max").value(maxMemory).build());
            result.add(ActuatorDto.builder().name("memory.usage.percent").value(Math.round(memoryUsage)).build());

        }catch (Exception e){
            log.error("cpu,mem monitoring error:{}",e.getMessage());
        }
        //log.debug("metric: {}",this.gson.toJson(result));
        return result;
    }

    public String getLocalHost(){
        String result = "0.0.0.0";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            result = localHost.getHostAddress();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return result;
    }
    /**
     *
     * @param duration
     */
    @Async
    public void CPULoadGenerator(long duration) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < (duration*1000)) {
            for (int i = 0; i < 100000; i++) {
                Math.sqrt(i);
                log.debug("CPULoadGenerator cont: {}",i);
            }
        }
    }

    /**
     *
     * @param duration : sec
     */
    @Async
    public void MEMORYLoadGenerator(long duration) {
        int elementSize = 1024;
        int numElements = 100000;
        long startTime = System.currentTimeMillis();

        List<byte[]> memoryList = new ArrayList<>();

        int count = 0;
        while (System.currentTimeMillis() - startTime < (duration*1000)) {
            byte[] element = new byte[elementSize];
            memoryList.add(element);
            log.debug("MEMORYLoadGenerator cont: {}",count++);
        }
    }

    /**
     *
     * @return
     */
    public  void getRandomLineFromFile() {
        try{
            if (!this.logLine.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(this.logLine.size());
                log.info(this.logLine.get(randomIndex));
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
