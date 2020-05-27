package fr.dawan.chouetteacademy.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Tag extends BaseEntity {
    private String name;
    @ManyToMany
    private List<Course> courses;

}
