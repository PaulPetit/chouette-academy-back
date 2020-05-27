package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.BaseDTO;
import fr.dawan.chouetteacademy.dto.CategoryListDTO;
import fr.dawan.chouetteacademy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController extends BaseController {

    @GetMapping("/api/categories")
    public ResponseEntity<CategoryListDTO> getCategories() {
        CategoryListDTO dto = categoryService.getCategoryListDTO();
        return getResponse(dto, null);
    }
}
