package fr.dawan.chouetteacademy.dto.courses;

import fr.dawan.chouetteacademy.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseByCategoryDTO extends BaseDTO {
    private Long id;
    private String name;
    private String description;
    private String pictureUrl;
    private Long nbCourses;
    List<CourseDTO> courses= new ArrayList<>();
}
