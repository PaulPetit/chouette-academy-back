package fr.dawan.chouetteacademy.respository;

import fr.dawan.chouetteacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);
    User findByEmail(String email);
    User findByUserNameOrEmail(String userName, String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscribedCourses WHERE u.userName=:userName")
    User findUserWithCourses(@Param("userName") String userName);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscribedCourses c WHERE c.id=:idcourse and u.id=:iduser")
    User findUserSubscribedCourse(@Param("iduser") long iduser, @Param("idcourse") long idcourse);

}
