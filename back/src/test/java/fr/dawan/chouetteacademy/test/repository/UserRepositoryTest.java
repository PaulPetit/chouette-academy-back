package fr.dawan.chouetteacademy.test.repository;

import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.respository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
public class UserRepositoryTest {

    @Autowired
    UserRepository repo;

    private final String USER_NAME = "testUser";
    private final String USER_EMAIL = "test@domain.com";

    @Test
    public void test() {
        repo.deleteAll();

        User u = new User();

        u.setEmail(USER_EMAIL);
        u.setUserName(USER_NAME);
        repo.saveAndFlush(u);

        List<User> users = repo.findAll();
        assertEquals(1, users.size());

        User u2 = repo.findByUserName(USER_NAME);
        assertEquals(USER_NAME, u2.getUserName());
        assertEquals(USER_EMAIL, u2.getEmail());

        repo.deleteAll();

        users = repo.findAll();
        assertEquals(0, users.size());

    }
}
