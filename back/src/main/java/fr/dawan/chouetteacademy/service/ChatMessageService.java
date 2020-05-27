package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.dto.ChatMessageDTO;
import fr.dawan.chouetteacademy.dto.ChatMessageListDTO;
import fr.dawan.chouetteacademy.entity.Chat;
import fr.dawan.chouetteacademy.entity.ChatMessage;
import fr.dawan.chouetteacademy.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service gérant les messages des chats
 */

@Service
public class ChatMessageService extends BaseService {

    // Ajoout d'un message de chat
    public void create(Chat chat, User user, String content) {
        if (chat == null || user == null || content == null) exceptionService.throwExceptionBadRequest();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(user);
        chatMessage.setChat(chat);
        chatMessage.setContent(content);
        chatMessageRepository.saveAndFlush(chatMessage);
    }

    // Retourne les messages d'un chat entre deux dates
    public List<ChatMessageDTO> findAllByChatAndDateTimeBetween(Chat chat, Long startDateTime, Long endDateTime) {

        List<ChatMessageDTO> dto = new ArrayList<ChatMessageDTO>();
        List<ChatMessage> liste = chatMessageRepository.findAllByChatAndTimestampBetweenOrderByTimestampAsc(chat, startDateTime, endDateTime);

        for (ChatMessage cm : liste) {
            ChatMessageDTO d = new ChatMessageDTO();
            d.setId(cm.getId());
            d.setContent(cm.getContent());
            d.setUser(cm.getUser().getUserName());
            d.setTimestamp(cm.getTimestamp());
            dto.add(d);
        }
        return dto;
    }

    // Retourne sous forme de DTO les messages d'un chat entre deux dates
    public ChatMessageListDTO findAllByChatAndDateTimeBetweenDTO(Chat chat, Long startDateTime, Long endDateTime) {
        ChatMessageListDTO dto = new ChatMessageListDTO();
        dto.setMessages(findAllByChatAndDateTimeBetween(chat, startDateTime, endDateTime));
        return dto;
    }
}
