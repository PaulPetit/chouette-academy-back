package fr.dawan.chouetteacademy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Chat extends BaseEntity {

    @OneToMany(mappedBy = "chat")
    private Collection<ChatMessage> chatMessages;

    @OneToOne(mappedBy = "chat")
    private Course course;

}
