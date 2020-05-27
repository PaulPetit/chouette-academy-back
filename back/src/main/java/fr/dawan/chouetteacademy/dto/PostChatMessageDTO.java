package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostChatMessageDTO extends BaseDTO {
    private String chatId;
    private String content;
}
