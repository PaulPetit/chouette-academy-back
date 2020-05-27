package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.dto.CategoryDTO;
import fr.dawan.chouetteacademy.dto.CategoryListDTO;
import fr.dawan.chouetteacademy.entity.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des catéories
 */

@Service
public class CategoryService extends BaseService {

    // retourne toutes les catégories
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Ajout d'une catégorie dans le système
    public Category create(String name, String description, String pictureUrl) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setPictureUrl(pictureUrl);
            categoryRepository.saveAndFlush(category);
            return category;
        }
        return null;
    }

    // Retourne la liste des catégories sous forme de DTO
    public CategoryListDTO getCategoryListDTO() {
        List<Category> categories = categoryRepository.findByOrderByNameAsc();
        List<CategoryDTO> categoriesDTO = new ArrayList<CategoryDTO>();

        for (Category categ : categories) {
            CategoryDTO categDTO = new CategoryDTO();
            categDTO.setId(categ.getId());
            categDTO.setName(categ.getName());
            categDTO.setDescription(categ.getDescription());
            categDTO.setPictureUrl(categ.getPictureUrl());
            categoriesDTO.add(categDTO);
        }
        CategoryListDTO dto = new CategoryListDTO();
        dto.setCategories(categoriesDTO);
        return dto;
    }

    // Retourne une catégorie d'après son ID
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Retourne une catégorie d'après son nom
    public Category findByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    // Test l'existence d'une catégorie par son ID
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }
}
