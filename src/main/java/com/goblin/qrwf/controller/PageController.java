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

    /**
     * page servlet
     * @param model
     * @return
     */
    @RequestMapping(value = {"/", "/index" ,"/home"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("page",  "index" );
        return "index";
    }

    /**
     * get monitoring metric (60 sec)
     */
    @Scheduled(cron="0 0/1 * * * *")
    public void monitoring() {

        monitoringService.writerMetric();
    }

    /**
     * generator error log (10 sec)
     */
    @Scheduled(cron="0/10 * * * * *")
    public void randomLog() {
        try {
            monitoringService.generateRandomException();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * cpu load generator (30sec)
     */
    @Scheduled(cron="0/3 * * * * *")
    public void cpuLoadGen() {
        monitoringService.CPULoadGenerator(60);
    }

    /**
     * memory load generator (30sec)
     */
//    @Scheduled(cron="0/30 * * * * *")
//    public void memLoadGen() {
//        monitoringService.MEMORYLoadGenerator(60);
//    }

}
