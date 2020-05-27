package fr.dawan.chouetteacademy.entity;

import fr.dawan.chouetteacademy.model.CourseAccessType;
import fr.dawan.chouetteacademy.model.CourseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor
public class Course extends BaseEntity {

    private String title;

    @Type(type="text")
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseStatus status=CourseStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    private CourseAccessType accessType=CourseAccessType.FREE;

    private String pictureUrl;

    private Long timestampCreation;
    private Long timestampStreamPlanned;
    private Long timestampStreamStart;

    @Column(unique=true)
    private String slug;

    public void setCreationDate() {
        this.timestampCreation  = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    @ManyToOne()
    private User owner;

    @OneToOne()
    private Chat chat;

    @ManyToOne()
    private Category category;

    @ManyToMany(mappedBy = "courses")
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany(mappedBy = "subscribedCourses")
    private List<User> students = new ArrayList<>();
}
