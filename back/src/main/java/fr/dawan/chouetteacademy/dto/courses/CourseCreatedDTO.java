package fr.dawan.chouetteacademy.dto.courses;

import fr.dawan.chouetteacademy.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseCreatedDTO extends BaseDTO {
    private Long id;
    private String slug;
    private Integer status;
    private String message;
}
