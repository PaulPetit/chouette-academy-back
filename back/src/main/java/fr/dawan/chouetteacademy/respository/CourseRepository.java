package fr.dawan.chouetteacademy.respository;

import fr.dawan.chouetteacademy.entity.Category;
import fr.dawan.chouetteacademy.entity.Course;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByCategory(Category category);

    List<Course> findAllByOwner(User user);

    List<Course> findByTitleContainsOrDescriptionContains(String q1, String q2);

    List<Course> findAllByStudentsContains(User user);

    boolean existsBySlug(String slug);

    List<Course> findTop6ByStatusOrderByTimestampStreamPlannedAsc(CourseStatus status);

    List<Course> findTop6ByStatus(CourseStatus status);

    @Query("FROM Course c LEFT JOIN FETCH c.tags WHERE c.id=:idcourse")
    Course findByIdWithTags(@Param("idcourse") long idcourse);

    @Query("FROM Course c LEFT JOIN FETCH c.students WHERE c.id=:idcourse")
    Course findByIdWithStudents(@Param("idcourse") long idcourse);

}
