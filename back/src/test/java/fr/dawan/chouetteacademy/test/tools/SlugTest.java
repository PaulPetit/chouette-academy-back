package fr.dawan.chouetteacademy.test.tools;

import fr.dawan.chouetteacademy.tools.Slug;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("TEST")
public class SlugTest {

    @Test
    public void makeTest() {
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
        assertEquals("xyyddfz", Slug.make("XΦ๚ѬථསYYྎddfොZ"));
    }
}
