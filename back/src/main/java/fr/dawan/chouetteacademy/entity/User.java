package fr.dawan.chouetteacademy.entity;

import fr.dawan.chouetteacademy.dto.RegisterDTO;
import fr.dawan.chouetteacademy.tools.Security;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "ca_user")
public class User extends BaseEntity {

    private String userName;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private String streamKey;

    private String twitterUrl;
    private String linkedInUrl;
    private String websiteUrl;

    @Type(type="text")
    private String bio;         // todo : a gérer

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "owner")
    private List<Course> ownedCourses = new ArrayList<>();

    @ManyToMany
    private List<Course> subscribedCourses  = new ArrayList<>();;

    public User() {
        generateStreamKey();
    }

    public User(String userName, String email, String password) {
        this();
        this.userName = userName;
        this.email = email;
        this.setCreationDate();
        this.setPasswordHash(password);
    }

    public User(RegisterDTO dto) {
        this();
        this.userName = dto.getUserName();
        this.email = dto.getEmail();
        this.setCreationDate();
        this.setPasswordHash(dto.getPassword());
    }

    // Enregistre le password sous forme de hash
    public void setPasswordHash(String password) {
        this.passwordHash = Security.sha256(password);
    }

    // Date de création du compte user
    public void setCreationDate() {
        this.creationDate = LocalDateTime.now();
    }

    // Test si clearPassword "hashé" == user.passwordHash
    public boolean testPassword(String clearPassword) {
        String reqPasswordHash = Security.sha256(clearPassword);
        return this.getPasswordHash().equals(reqPasswordHash);
    }

    public List<Course> getSubscribedCourses() {
        return subscribedCourses;
    }

    public void setSubscribedCourses(List<Course> subscribedCourses) {
        this.subscribedCourses = subscribedCourses;
    }

    public void generateStreamKey() {
        String key = Security.randomString(12);
        setStreamKey(key);
    }
}
