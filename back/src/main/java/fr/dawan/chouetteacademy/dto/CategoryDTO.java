package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryDTO extends BaseDTO {
    private Long id;
    private String name;
    private String description;
    private String pictureUrl;
}
