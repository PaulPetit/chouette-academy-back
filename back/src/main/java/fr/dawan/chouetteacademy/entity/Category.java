package fr.dawan.chouetteacademy.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Category extends BaseEntity {
    private String name;

    @Type(type="text")
    private String description;
    private String pictureUrl;
    @OneToMany(mappedBy = "category")
    private Collection<Course> courses;
}
