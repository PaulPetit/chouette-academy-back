package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends BaseDTO {
    private Long id;
    private String userName;
    private String fullName;
    private String pictureUrl;
    private String bio;
    private String twitterUrl;
    private String linkedInUrl;
    private String websiteUrl;
}


