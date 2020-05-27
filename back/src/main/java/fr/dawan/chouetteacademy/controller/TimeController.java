package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.TimeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
public class TimeController extends BaseController {
    @GetMapping("/api/time")
    public ResponseEntity<TimeDTO> getTime() {
        Long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        TimeDTO dto = new TimeDTO();
        dto.setTimestamp(timestamp);
        return ResponseEntity.ok(dto);
    }
}
