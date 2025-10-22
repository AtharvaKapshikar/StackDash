package com.StackDash.Controller;

import com.StackDash.Entity.SessionLog;
import com.StackDash.Repository.SessionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/session-history")
public class SessionHistoryController {

    private static final Logger log = LoggerFactory.getLogger(SessionHistoryController.class);
    @Autowired
    private SessionLogRepository sessionLogRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getSessionHistory(@PathVariable Long userId) {
        try {
            log.trace("into getsessionLog cotroller method:{}", userId);
           // Pageable limit = PageRequest.of(0, 3, Sort.by("loginTime").descending());
            List<SessionLog> logs = sessionLogRepository.findByUserId(userId);
            if(logs.size()<2){
                return ResponseEntity.ok(logs);
            }
            List<SessionLog> latestLog = logs.stream().skip((long) logs.size() -2).limit(1).collect(Collectors.toList());

            log.trace("logs : {}", logs);
            log.trace("latest logs : {}", latestLog);
            return ResponseEntity.ok(latestLog);
        }catch (Exception ex){
            log.error("Error while getting session logs: {}", ex.getMessage());
            return ResponseEntity.status(401).body("Error while getting session logs: {}\", ex.getMessage()");
        }
    }
}

