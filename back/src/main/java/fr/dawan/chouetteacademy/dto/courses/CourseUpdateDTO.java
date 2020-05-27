package fr.dawan.chouetteacademy.dto.courses;

import fr.dawan.chouetteacademy.dto.BaseDTO;
import fr.dawan.chouetteacademy.model.CourseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseUpdateDTO extends BaseDTO {
    private Long id;
    private Long ownerId;
    private String title;
    private String description;
    private Long timestampStreamPlanned;
    private Long categoryId;
    private CourseStatus status;
    private Long[] tagsId;
}
