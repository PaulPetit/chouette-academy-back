package fr.dawan.chouetteacademy.respository;

import fr.dawan.chouetteacademy.entity.Chat;
import fr.dawan.chouetteacademy.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Retourne des messages de chat entre deux dates
    List<ChatMessage> findAllByChatAndTimestampBetweenOrderByTimestampAsc(Chat chat, Long startDateTime, Long endDateTime);

}
