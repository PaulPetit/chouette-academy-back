package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    private String password;
    private String newPassword;
}
