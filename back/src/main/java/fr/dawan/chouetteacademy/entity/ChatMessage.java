package fr.dawan.chouetteacademy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Getter
@Setter
public class ChatMessage extends BaseEntity {

    @ManyToOne()
    private Chat chat;

    @OneToOne()
    private User user;

    private String content;

    private Long timestamp;

    public ChatMessage() {
        timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
