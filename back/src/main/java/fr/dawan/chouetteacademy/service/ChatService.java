package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.entity.Chat;
import org.springframework.stereotype.Service;

/**
 * Service de gestion des chats
 */

@Service
public class ChatService extends BaseService {

    // Retourne un chat par son ID
    public Chat findById(long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }
}
