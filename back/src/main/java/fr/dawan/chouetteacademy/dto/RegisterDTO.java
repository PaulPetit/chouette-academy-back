package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDTO extends BaseDTO {
    private String userName;
    private String email;
    private String password;
}
