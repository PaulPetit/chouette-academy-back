package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.*;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController extends BaseController {

    @Value("${streamserver.baseurl.owner}")
    private String streamUrl;

    @GetMapping("/api/user/public/{id}")
    public ResponseEntity<UserDTO> getUser(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @PathVariable(name = "id") Long id) {

        UserSession session = sessionService.getUserSession(httpTokenHash);

        if (id == null) exceptionService.throwExceptionBadRequest();

        UserDTO dto = userService.getDtoById(id);
        return getResponse(dto, session);
    }

    @GetMapping("/api/user/edit")
    public ResponseEntity<UserPrivateDTO> getUserEdit(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);
        if (session == null) exceptionService.throwExceptionUnauthorized();

        UserPrivateDTO dto = userService.getPrivateDto(session.getUser());
        return getResponse(dto, session);
    }

    @GetMapping("/api/user/streamurl")
    public ResponseEntity<UserStreamUrlDTO> getUserStreamUrl(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);
        UserStreamUrlDTO dto = new UserStreamUrlDTO();
        dto.setStreamKey(session.getUser().getStreamKey());
        dto.setServerUrl(streamUrl);
        return getResponse(dto, session);
    }

    @PutMapping("/api/user/update")
    public ResponseEntity<ResponseDTO> putUser(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @RequestBody UserPutDTO dto) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        if (dto == null) exceptionService.throwExceptionBadRequest();

        return getResponse(userService.update(dto, session), session);
    }

    @PostMapping("/api/user/picture")
    public ResponseEntity<ResponseDTO> uploadFile(@RequestHeader(name = "Authorization", required = false) String httpTokenHash,
                                                  @RequestParam("file") MultipartFile file) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        userService.updateProfileImage(session.getUser(), file);

        return getResponse(MessageStatus.IMAGE_UPLOADED, session, HttpStatus.OK);
    }

    @PostMapping("/api/user/updatepassword")
    public ResponseEntity<ResponseDTO> checkPassword(@RequestHeader(name = "Authorization", required = false) String httpTokenHash,
                                                           @RequestBody UpdatePasswordDTO dto) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);
        ResponseDTO responseDTO = userService.updatePassword(session.getUser(), dto.getPassword(), dto.getNewPassword());
        return getResponse(responseDTO, session);
    }
}
