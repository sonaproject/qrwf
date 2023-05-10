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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.web.client.RestTemplate;

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

    private InfluxDBClient client;

    @Autowired
    private Gson gson;

    @PostConstruct
    public void init(){
        this.client = InfluxDBClientFactory.create(this.url, this.token.toCharArray());
    }
    @PreDestroy
    public void cleanup() {
        this.client.close();
    }
    /**
     *
     */
    public String writerMetric(){
        ArrayList<ActuatorDto> list = this.getMetrics();
        List<Point> points = new ArrayList<>();

        for(ActuatorDto data: list){
            points.add(Point.measurement("monitoring")
                    .addTag("service", "qrwf")
                    .addField(data.getName(), data.getValue())
                    .time(Instant.now(), WritePrecision.NS));
        }
        try (WriteApi writeApi = client.getWriteApi()) {
            log.debug("Start save data: {}",points.size());
            for (Point point : points) {
                writeApi.writePoint(bucket, org, point);
            }
        } catch (Exception e) {
           log.error(e.getMessage());
        }
        return String.valueOf(points.size());
    }

    public ArrayList<ActuatorDto> getMetrics(){
        ArrayList<ActuatorDto> result = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        try {
            String[] metricName = {"jvm.memory.used","jvm.memory.committed","jvm.gc.memory.allocated","jvm.gc.memory.promoted","process.cpu.usage"};

            for(String metric: metricName){
                ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/management/metrics/"+metric, String.class);
                String healthData = response.getBody();
                JvmMetricDto data = gson.fromJson(healthData,JvmMetricDto.class);
                result.add(ActuatorDto.builder().name(metric).value(data.getMeasurements().get(0).getValue()).build());
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return result;
    }
}
