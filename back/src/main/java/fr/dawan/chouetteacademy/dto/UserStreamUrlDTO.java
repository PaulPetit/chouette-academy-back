package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserStreamUrlDTO extends BaseDTO {
    String serverUrl;
    String streamKey;
}
