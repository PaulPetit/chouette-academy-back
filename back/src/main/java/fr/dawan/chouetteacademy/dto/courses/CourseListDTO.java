package fr.dawan.chouetteacademy.dto.courses;


import fr.dawan.chouetteacademy.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseListDTO extends BaseDTO {
    private Long nbCourses;
    List<CourseDTO> courses = new ArrayList<CourseDTO>();
}
