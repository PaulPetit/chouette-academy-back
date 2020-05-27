package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserPrivateDTO extends UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime creationDate;
    private List<Long> ownedCoursesId = new ArrayList<>();
    private List<Long> subscribedCoursesId = new ArrayList<>();
}


