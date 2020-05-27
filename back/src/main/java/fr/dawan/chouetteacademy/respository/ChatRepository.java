package fr.dawan.chouetteacademy.respository;

import fr.dawan.chouetteacademy.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
