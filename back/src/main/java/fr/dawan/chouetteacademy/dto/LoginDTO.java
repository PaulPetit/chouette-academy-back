package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter @Setter
public class LoginDTO extends BaseDTO {

    @NotNull
    private String login;

    @NotNull
    private String password;
}
