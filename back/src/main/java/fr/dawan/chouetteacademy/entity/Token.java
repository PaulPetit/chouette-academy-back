package fr.dawan.chouetteacademy.entity;

import fr.dawan.chouetteacademy.Conf;
import fr.dawan.chouetteacademy.tools.Security;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor

public class Token extends BaseEntity {

    private String hashKey;
    private LocalDateTime expire;

    public Token(User user) {
        this.renew();
        this.user = user;
    }

    // Renouvel le token : nouveau hashKey, nouvelle date d'expiration
    public void renew() {
        this.hashKey = Security.randomString(Conf.TOKEN_SIZE);
        this.expire = LocalDateTime.now().plusMinutes(Conf.TOKEN_LIFE);
    }

    // Supprime les caractères inutiles dans la variable Authorization du header ("Bearer "...)
    public static String trimHttp(String str) {
        return str.substring(7);
    }

    // Test si la date de validité du token n'est pas expiré
    public boolean isValide() {
        LocalDateTime expire = LocalDateTime.now();
        return this.expire.isAfter(expire);
    }

    @OneToOne()
    private User user;

}
