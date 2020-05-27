package fr.dawan.chouetteacademy.test.controller;

import fr.dawan.chouetteacademy.respository.CourseRepository;
import kong.unirest.Headers;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
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
public class UserControllerTest {

    @Autowired
    public CourseRepository courseRepository;

    public final String BASE_URL = "http://localhost:8181/api";

    @Test
    public void test() {

        HttpResponse<String> response = Unirest.post(BASE_URL + "/login")
                .header("Content-Type", "application/json")
                .body("{\n\t\"login\":\"frogez\",\n\t\"password\":\"totototo\"\n}")
                .asString();

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatus());
        Headers h = response.getHeaders();
        assertTrue(h.containsKey("X-Token"));
        System.out.println("X-Token : " + h.get("X-Token"));
    }
}
