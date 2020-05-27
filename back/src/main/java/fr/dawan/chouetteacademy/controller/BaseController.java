package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.Conf;
import fr.dawan.chouetteacademy.dto.BaseDTO;
import fr.dawan.chouetteacademy.dto.ResponseDTO;
import fr.dawan.chouetteacademy.lab.MapDTO;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import fr.dawan.chouetteacademy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
@CrossOrigin(origins = Conf.CROSS_ORIGIN_HOST, exposedHeaders = {Conf.EXPOSED_HEADERS})
public abstract class BaseController {

    @Autowired protected ExceptionService exceptionService;
    @Autowired protected LoginService loginService;
    @Autowired protected SessionService sessionService;
    @Autowired protected UserService userService;
    @Autowired protected CategoryService categoryService;
    @Autowired protected CourseService courseService;
    @Autowired protected ChatMessageService chatMessageService;
    @Autowired protected ChatService chatService;
    @Autowired protected RegisterService registerService;

    protected <T extends BaseDTO> ResponseEntity<T> getResponse(T dto, UserSession session, HttpStatus status) {
        HttpHeaders h = new HttpHeaders();

        if (session != null) {
            h.set("X-Token", session.getToken().getHashKey());
        }
        return new ResponseEntity<T>(dto, h, status);
    }

    protected <T extends BaseDTO> ResponseEntity<T> getResponse(T dto, UserSession session) {
        HttpHeaders h = new HttpHeaders();

        if (session != null) {
            h.set("X-Token", session.getToken().getHashKey());
        }
        return new ResponseEntity<T>(dto, h, HttpStatus.OK);
    }

    protected ResponseEntity<ResponseDTO> getResponse(MessageStatus messageStatus, UserSession session, HttpStatus status) {
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setStatus(status.value());
        responseDto.setMessage(messageStatus.toString());
        return getResponse(responseDto, session, status);
    }

    protected ResponseEntity<ResponseDTO> getResponse(MessageStatus messageStatus, Map<String, String> data, UserSession session) {
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setData(data);
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage(messageStatus.toString());
        return getResponse(responseDto, session, HttpStatus.OK);
    }

    protected ResponseEntity<ResponseDTO> getResponse(MessageStatus messageStatus) {
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage(messageStatus.toString());
        return getResponse(responseDto, null, HttpStatus.OK);
    }
}
