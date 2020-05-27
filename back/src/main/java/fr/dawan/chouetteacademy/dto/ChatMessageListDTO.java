package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ChatMessageListDTO extends BaseDTO {
    private List<ChatMessageDTO> messages = new ArrayList<ChatMessageDTO>();
}
