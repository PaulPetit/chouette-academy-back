package fr.dawan.chouetteacademy.test.service;

import fr.dawan.chouetteacademy.dto.LoginDTO;
import fr.dawan.chouetteacademy.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
public class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Test
    public void test() {

        LoginDTO loginDTO = new LoginDTO();
        // login et password = bons
        loginDTO.setLogin("frogez");
        loginDTO.setPassword("totototo");
        assertNotNull(loginService.login(loginDTO));

        // login = bon, password = mauvais
        loginDTO.setLogin("frogez");
        loginDTO.setPassword("xyz");
        try {
            loginService.login(loginDTO);
        } catch (ResponseStatusException e) {
            assert(e.getMessage().contains("WRONG_LOGIN"));
        }

        // login et password null
        loginDTO.setLogin(null);
        loginDTO.setPassword(null);
        try {
            loginService.login(loginDTO);
        } catch (ResponseStatusException e) {
            assert(e.getMessage().contains("WRONG_LOGIN"));
        }

    }
}
