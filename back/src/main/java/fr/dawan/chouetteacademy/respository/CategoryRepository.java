package fr.dawan.chouetteacademy.respository;

import fr.dawan.chouetteacademy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String categoryName);

    List<Category> findByOrderByNameAsc();

    boolean existsById(Long id);
}
