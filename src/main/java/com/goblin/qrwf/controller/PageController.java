package com.goblin.qrwf.controller;

import com.goblin.qrwf.service.MonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : com.goblin.qrwf.controller
 * fileName : PageController
 * author : goodhyoju
 * date : 2023/05/09 3:31 PM
 * description :
 */
@Slf4j
@CrossOrigin(origins = "*")
@Controller
public class PageController {
    @Autowired
    private MonitoringService monitoringService;

    @RequestMapping(value = {"/", "/index" ,"/home"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("page",  "index" );
        return "index";
    }


    @Scheduled(cron="0 0/1 * * * *")
    public String monitoring() {
        log.debug("Monitoring Run~");
        return monitoringService.writerMetric();
    }

    /**
     *
     * @param type
     * @param duration
     * @return
     */
    @GetMapping("/loadGenerator/{type}/{duration}")
    public ResponseEntity<String> clusterTableData(@PathVariable("type") String type, @PathVariable("duration") long duration) {
        String result = "";
        log.debug("loader start: type-{},duration-{}",type,duration);
        if(type.equals("cpu")){
            monitoringService.CPULoadGenerator(duration);
        }else{
            monitoringService.MEMORYLoadGenerator(duration);
        }
        result = type + "Load generation proceeds for "+duration+" seconds.";
        return ResponseEntity.ok(result);

    }
}
