package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.LoginDTO;
import fr.dawan.chouetteacademy.dto.ResponseDTO;
import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import fr.dawan.chouetteacademy.service.LoginService;
import fr.dawan.chouetteacademy.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController extends BaseController {

    @PostMapping("/api/login")
    public ResponseEntity<ResponseDTO> apiLogin(@RequestHeader HttpHeaders headers, @Validated @RequestBody LoginDTO dto) {
        Token token = loginService.login(dto);
        UserSession session = new UserSession();
        session.setToken(token);
        session.setUser(token.getUser());

        Map<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(session.getUser().getId()));

        return getResponse(MessageStatus.LOGIN_OK, data, session);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<ResponseDTO> apiLogout(@RequestHeader(name = "Authorization", required = true) String httpTokenHash) {
        loginService.logout(httpTokenHash);
        return getResponse(MessageStatus.LOGOUT_OK);
    }
}
