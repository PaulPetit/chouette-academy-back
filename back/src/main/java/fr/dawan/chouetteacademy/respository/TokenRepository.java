package fr.dawan.chouetteacademy.respository;

import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByHashKey(String key);

    Token findByUser(User user);

    long deleteByHashKey(String key);
}
