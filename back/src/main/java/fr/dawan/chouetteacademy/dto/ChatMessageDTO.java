package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessageDTO extends BaseDTO {
    private Long id;
    private String user;
    private String content;
    private Long timestamp;
}
