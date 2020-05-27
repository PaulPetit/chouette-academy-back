package fr.dawan.chouetteacademy.dto.courses;

import fr.dawan.chouetteacademy.dto.BaseDTO;
import fr.dawan.chouetteacademy.model.CourseStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.*;

@Getter
@Setter
public class CourseDTO extends BaseDTO {
    private Long id; //
    private Long ownerId; //
    private String instructorName; //
    private Long instructorId;
    private String title; //
    private String description; //
    private Long timestampStreamPlanned; //
    private Long timestampCreation; //
    private String streamURL; //
    private Long chatId; //
    private Long categoryId; //
    private String categoryName; // --
    private Long nbSubcribers; //
    private String pictureUrl; //
    private Boolean subscribed; //
    private Boolean live; //
    private Boolean owned; //

    @Enumerated(EnumType.STRING)
    private CourseStatus status; //

    private List<Long> studentsId = new ArrayList<Long>(); //
}
