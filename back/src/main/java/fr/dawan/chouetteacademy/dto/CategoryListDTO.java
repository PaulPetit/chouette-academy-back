package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryListDTO extends BaseDTO {
    private List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
}
