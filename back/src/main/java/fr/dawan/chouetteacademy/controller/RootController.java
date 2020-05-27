package fr.dawan.chouetteacademy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController extends BaseController {
    @RequestMapping(value = {"/", "", "/api", "/api/"})
    public void getForbidden() {
        exceptionService.throwExceptionForbidden();
    }
}
