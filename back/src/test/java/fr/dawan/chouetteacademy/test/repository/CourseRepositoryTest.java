package fr.dawan.chouetteacademy.test.repository;

import fr.dawan.chouetteacademy.respository.CourseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
public class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @Test
    public void test() {
    }
}
