package fr.dawan.chouetteacademy.test;

import fr.dawan.chouetteacademy.entity.Course;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.respository.CourseRepository;
import fr.dawan.chouetteacademy.respository.UserRepository;
import fr.dawan.chouetteacademy.service.UserService;
import fr.dawan.chouetteacademy.tools.Slug;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
public class ChouetteAcademyApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
	private CourseRepository courseRepository;

    private User userFrogez;
    private User userPpetit;
    private Course course1;
	private Course course2;

    @Before
    public void initTests() {
        userFrogez = userRepository.findById(1L).orElse(null);
        userPpetit = userRepository.findById(2L).orElse(null);
        course1 = courseRepository.findById(1L).orElse(null);
		course2 = courseRepository.findById(2L).orElse(null);
    }

    @Test
    public void testUsers() {
        assertEquals("frogez", userFrogez.getUserName());
        assertEquals("ppetit", userPpetit.getUserName());
    }

    @Test
    public void testUsersCourses() {
    	assertNotNull(course1);
        assertTrue(userService.isUserSubscribedToCourse(userFrogez, course1));
		assertTrue(userService.isUserSubscribedToCourse(userPpetit, course2));

        assertFalse(userService.isUserSubscribedToCourse(userPpetit, course1));
    }

    @Test
    public void testMakeSlug() {
        assertEquals("", Slug.make(""));
        assertEquals("", Slug.make("-"));
        assertEquals("", Slug.make("--"));
        assertEquals("", Slug.make(" - - "));
        assertEquals("", Slug.make(" "));
        assertEquals("", Slug.make("   -    - "));
        assertEquals("", Slug.make(" -"));
        assertEquals("", Slug.make("- "));
        assertEquals("mon-titre-ici", Slug.make("      Mon       titre       ICI    "));
        assertEquals("hello-world", Slug.make("--Hello World"));
        assertEquals("hello-world", Slug.make("Hello World !!!"));
        assertEquals("a-e-e-e-e-i-i-i-o-o-u", Slug.make("à é è ê ë i ï î ô ö ù"));
        assertEquals("super-titre", Slug.make("--- super titre !!! ---"));
        assertEquals("mon-titre-fin", Slug.make("Mon Titre : \" ' / \\ $  (FIN) -- "));
        assertEquals("aaa-bbb-ccc", Slug.make("aAa &([])=+**!:bbB;,/.?<> cCC"));
        assertEquals("x", Slug.make(".\\ X '"));
    }

    @Test
    public void testCreateCourse() {


    }

}
