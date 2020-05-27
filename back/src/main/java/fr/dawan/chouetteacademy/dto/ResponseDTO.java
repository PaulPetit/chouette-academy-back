package fr.dawan.chouetteacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class ResponseDTO extends BaseDTO {
    private Integer status;
    private String message;
    private Map<String, String> data;
}
