package fr.dawan.chouetteacademy.lab;

import fr.dawan.chouetteacademy.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LabController extends BaseController {

    @PostMapping("/lab/test")
    public MapDTO lab1(@RequestBody MapDTO map) {
        return map;
    }
}
