package fr.dawan.chouetteacademy.model;


import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSession {
    private User user;
    private Token token;
}
