package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserPutDTO extends UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
}

