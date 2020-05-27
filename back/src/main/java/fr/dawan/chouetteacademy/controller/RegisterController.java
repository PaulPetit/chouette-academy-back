package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.ResponseDTO;
import fr.dawan.chouetteacademy.dto.RegisterDTO;
import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import fr.dawan.chouetteacademy.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@Transactional
public class RegisterController extends BaseController {

    @Autowired
    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/api/register")
    public @ResponseBody
    ResponseEntity<ResponseDTO> apiRegister(@RequestBody RegisterDTO registerDto) {
        Token token = registerService.register(registerDto);
        UserSession session = sessionService.getUserSession(token.getHashKey());
        return getResponse(MessageStatus.REGISTERED_OK, session, HttpStatus.OK);
    }
}
