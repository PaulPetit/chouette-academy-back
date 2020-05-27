package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.ChatMessageListDTO;
import fr.dawan.chouetteacademy.dto.PostChatMessageDTO;
import fr.dawan.chouetteacademy.dto.ResponseDTO;
import fr.dawan.chouetteacademy.entity.Chat;
import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import fr.dawan.chouetteacademy.service.ChatMessageService;
import fr.dawan.chouetteacademy.service.ChatService;
import fr.dawan.chouetteacademy.service.ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatMessageController extends BaseController {

    @PostMapping("/api/chat/message")
    public ResponseEntity<ResponseDTO> postChatMessage(@RequestHeader(name = "Authorization", required = true) String httpTokenHash,
                                                       @RequestBody PostChatMessageDTO dto) {

        UserSession session = sessionService.getUserSession(httpTokenHash);

        if (session != null ) {
            long chatId = Long.parseLong(dto.getChatId());
            Chat chat = chatService.findById(chatId);
            chatMessageService.create(chat, session.getUser(), dto.getContent());
        } else {
            exceptionService.throwExceptionUnauthorized();
        }
        return getResponse(MessageStatus.CREATED, session,HttpStatus.CREATED);
    }

    @GetMapping("/api/chat/{idChat}/messages")
    public ResponseEntity<ChatMessageListDTO> getChatMessage(@RequestHeader(name = "Authorization", required = true) String httpTokenHash,
                                                              @PathVariable(value = "idChat") Long idChat,
                                                              @RequestParam(value = "startDate") Long startDate,
                                                              @RequestParam(value = "endDate") Long endDate) {

        UserSession session = sessionService.getUserSession(httpTokenHash);

        if (session != null) {
            Chat chat = chatService.findById(idChat);
            ChatMessageListDTO dto = chatMessageService.findAllByChatAndDateTimeBetweenDTO(chat, startDate, endDate);
            return getResponse(dto, session);
        } else {
            exceptionService.throwExceptionUnauthorized();
        }
        return null;
    }
}
